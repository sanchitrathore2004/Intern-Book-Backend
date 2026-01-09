package com.internbook.internbook_backend.events;

import com.internbook.internbook_backend.dtos.TrieSubmissionDto;

public record SubmissionDeletedEvent(TrieSubmissionDto submission) {
}
