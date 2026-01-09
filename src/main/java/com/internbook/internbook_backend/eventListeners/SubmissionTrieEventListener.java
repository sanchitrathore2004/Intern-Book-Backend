package com.internbook.internbook_backend.eventListeners;

import com.internbook.internbook_backend.events.SubmissionCreatedEvent;
import com.internbook.internbook_backend.events.SubmissionDeletedEvent;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import com.internbook.internbook_backend.services.SubmissionService;
import com.internbook.internbook_backend.services.TrieService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SubmissionTrieEventListener {
    private final TrieService trieService;
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleSubmissionCreatedEvent(SubmissionCreatedEvent event) {
        submissionRepository.findWithAllDetails(event.submissionId())
                .ifPresent(submission -> {
                    trieService.indexSubmission(
                            submissionService.toTrieSubmissionDto(submission)
                    );
                });
    }

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleSubmissionDeletedEvent(SubmissionDeletedEvent event) {
        if(event.submission() != null) {
            trieService.removeSubmission(
                    event.submission()
            );
        }
    }
}
