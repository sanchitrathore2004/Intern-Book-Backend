package com.internbook.internbook_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmissionRequestDto {
    @NotNull(message = "Submission Id should not be null")
    private Integer submissionId;
}
