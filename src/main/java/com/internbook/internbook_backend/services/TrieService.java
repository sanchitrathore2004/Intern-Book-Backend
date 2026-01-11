package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.AutoCompleteResponseDto;
import com.internbook.internbook_backend.dtos.TrieSubmissionDto;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@RequiredArgsConstructor
public class TrieService {

    private static Integer MAX_PHRASE_PER_NODE;
    private static Integer MAX_IDS_PER_PHRASE;
    private static Integer MAX_IDS;

    private final TrieNode root = new TrieNode();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;

    @Value("${spring.trie.phrasePerNode}")
    public void setMaxPhrasePerNode(Integer value) {
        MAX_PHRASE_PER_NODE = value;
    }

    @Value("${spring.trie.idsPerNode}")
    public void setMaxIdsPerPhrase(Integer value) {
        MAX_IDS_PER_PHRASE = value;
    }

    @Value("${spring.trie.topKNode}")
    public void setMaxIds(Integer value) {
        MAX_IDS = value;
    }

    @Transactional(readOnly = true)
    public void buildIndexOnStartup() {
        submissionRepository.findAllWithDetails()
                .forEach(submission -> indexSubmission(
                        submissionService.toTrieSubmissionDto(submission)
                ));
    }

    public void indexSubmission(TrieSubmissionDto submission) {
        lock.writeLock().lock();
        try {
            var submissionId = submission.getId();
            var score = computeScore(submission);

            for(String phrase: extractPhrases(submission)) {
                insertPhrase(phrase, submissionId, score);
            }
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public void removeSubmission(TrieSubmissionDto submission) {
        lock.writeLock().lock();
        try {
            for(String phrase: extractPhrases(submission)) {
                removePhrase(phrase, submission.getId());
            }
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public List<AutoCompleteResponseDto> searchMetaData(String query) {
        if(query == null || query.isEmpty()) return List.of();

        String normalized = normalize(query);

        lock.readLock().lock();
        try {
            TrieNode node = root;
            for(char c: normalized.toCharArray()) {
                node = node.children.get(c);
                if(node == null) return List.of();
            }
            return node.getTopSuggestions();
        }
        finally {
            lock.readLock().unlock();
        }
    }

    private void insertPhrase(String phrase, Integer submissionId, Double score) {
        TrieNode node = root;

        for(char c: phrase.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());

            node.phrases
                    .computeIfAbsent(phrase, p -> new RankedIdSet())
                    .add(submissionId, score);

            node.trim(MAX_PHRASE_PER_NODE);
        }
    }

    private void removePhrase(String phrase, Integer submissionId) {
        TrieNode node = root;

        for(char c: phrase.toCharArray()) {
            node = node.children.get(c);
            if(node == null) return;

            RankedIdSet set = node.phrases.get(phrase);
            if(set != null) {
                set.remove(submissionId);
                if(set.topIds().isEmpty())
                    node.phrases.remove(phrase);
            }
        }
    }

    //  modify as per need
    private Double computeScore(TrieSubmissionDto submission) {
        var likes = Optional.ofNullable(submission.getLikeCount()).orElse(0);
        LocalDateTime created = Optional.ofNullable(submission.getCreatedAt()).orElse(LocalDateTime.now());

        long hoursOld = ChronoUnit.HOURS.between(created, LocalDateTime.now());
        double recencyBoost = Math.max(0, 1_000_000.0 / (hoursOld + 1));

        return likes * 10.0 + recencyBoost;
    }

    private Set<String> extractPhrases(TrieSubmissionDto submission) {
        Set<String> phrases = new HashSet<>();

        var company = submission.getCompany();
        var domain = submission.getDomain();

        // Company name
        if (company != null && company.getName() != null && !company.getName().isBlank()) {
            Arrays.stream(company.getName().split("\\s+"))
                    .map(this::normalize)
                    .filter(s -> !s.isBlank())
                    .forEach(phrases::add);
        }

        // Domain name
        if (domain != null && domain.getDomainName() != null && !domain.getDomainName().isBlank()) {
            Arrays.stream(domain.getDomainName().split("\\s+"))
                    .map(this::normalize)
                    .filter(s -> !s.isBlank())
                    .forEach(phrases::add);
        }

        // Company + Domain combo
        if (company != null && domain != null &&
                company.getName() != null &&
                domain.getDomainName() != null) {

            phrases.add(normalize(
                    company.getName() + " " + domain.getDomainName()
            ));
        }

        // Author & department
        if (submission.getAuthorId() != null && !submission.getIsAnonymous()) {
            if (submission.getName() != null)
                phrases.add(normalize(submission.getName()));

            if (submission.getDepartment() != null && submission.getDepartment().getName() != null)
                phrases.add(normalize(submission.getDepartment().getName()));
        }

        // Club positions
        if (submission.getClubPositions() != null && !submission.getClubPositions().isBlank()) {
            Arrays.stream(submission.getClubPositions().split("\\s+"))
                    .map(this::normalize)
                    .filter(s -> !s.isBlank())
                    .forEach(phrases::add);
        }

        return phrases;
    }


    private String normalize(String text) {
        return text == null ? "" :
                text.toLowerCase(Locale.ROOT)
                        .replaceAll("[^a-z0-9\\s]", "")
                        .trim();
    }

    private static class TrieNode {
        final Map<Character, TrieNode> children = new ConcurrentHashMap<>();
        private final Map<String, RankedIdSet> phrases = new HashMap<>();

        void trim(int maxPhrases) {
            if(phrases.size() <= maxPhrases) return;

            phrases.entrySet().stream()
                    .min(Comparator.comparingDouble(e -> e.getValue().minScore()))
                    .map(Map.Entry::getKey).ifPresent(phrases::remove);

        }

        List<AutoCompleteResponseDto> getTopSuggestions() {
            return phrases.entrySet().stream()
                    .sorted((a, b) ->
                            Integer.compare(
                                    b.getValue().size(),
                                    a.getValue().size()
                            ))
                    .limit(MAX_IDS_PER_PHRASE)
                    .map(e -> new AutoCompleteResponseDto(
                            e.getKey(),
                            e.getValue().topIds()
                    ))
                    .toList();
        }
    }

    private static class RankedIdSet {

        private final Map<Integer, Double> scores = new HashMap<>();

        void add(Integer submissionId, Double score) {
            scores.put(submissionId, score);

            if(scores.size() > MAX_IDS) {
                removeLowest();
            }
        }

        void remove(Integer submissionId) {
            scores.remove(submissionId);
        }

        boolean isEmpty() {
            return scores.isEmpty();
        }

        int size() {
            return scores.size();
        }

        double minScore() {
            return scores.values().stream()
                    .min(Double::compare)
                    .orElse(0.0);
        }

        List<Integer> topIds() {
            return scores.entrySet().stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                    .map(Map.Entry::getKey)
                    .toList();
        }

        private void removeLowest() {
            scores.entrySet().stream()
                    .min(Comparator.comparingDouble(Map.Entry::getValue))
                    .map(Map.Entry::getKey).ifPresent(scores::remove);
        }
    }
}
