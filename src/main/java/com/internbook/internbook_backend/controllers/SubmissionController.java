package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.SaveRoleRequestDto;
import com.internbook.internbook_backend.dtos.SaveSubmissionRequestDto;
import com.internbook.internbook_backend.dtos.SaveCompanyRequestDto;
import com.internbook.internbook_backend.services.SubmissionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/submission")
public class SubmissionController {
    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<?> saveSubmission(
            @Valid @RequestBody SaveSubmissionRequestDto request
            ) {
        var response = submissionService.saveSubmission(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", response));
    }

    @PostMapping("/company")
    public ResponseEntity<?> saveCompany(
            @Valid @RequestBody SaveCompanyRequestDto request
    ) {
        var response = submissionService.saveCompany(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", response));
    }

    @PostMapping("/role")
    public ResponseEntity<?> saveRole(
            @Valid @RequestBody SaveRoleRequestDto request
    ) {
        var response =  submissionService.saveRole(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", response));
    }

    @PatchMapping("/{submission_id}")
    public ResponseEntity<?> editSubmission(
            @NotNull(message = "Submission Id should be not null")
            @PathVariable(name = "submission_id")
            Integer submissionId,
            @Valid @RequestBody  SaveSubmissionRequestDto request
    ){
        var response = submissionService.updateSubmission(submissionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", response)
        );
    }

    @DeleteMapping("/{submission_id}")
    public ResponseEntity<?> deleteSubmission(
            @NotNull(message = "Submission Id should be not null")
            @PathVariable(name = "submission_id")
            Integer submissionId
    ){
        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
