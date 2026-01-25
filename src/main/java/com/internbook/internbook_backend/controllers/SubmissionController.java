package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.common.pagination.CursorPage;
import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.services.SubmissionService;
import com.internbook.internbook_backend.services.TrieService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/submission")
public class SubmissionController {
    private final SubmissionService submissionService;
    private final TrieService trieService;

    @PostMapping
    public ResponseEntity<SubmissionResponseDto> saveSubmission(
            @Valid @RequestBody SaveSubmissionRequestDto request
            ) {
        var response = submissionService.saveSubmission(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/company")
    public ResponseEntity<CompanyResponseDto> saveCompany(
            @Valid @RequestBody SaveCompanyRequestDto request
    ) {
        var response = submissionService.saveCompany(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{submission_id}")
    public ResponseEntity<SubmissionResponseDto> getSubmissionById(
            @PathVariable(name="submission_id") Integer submissionId
    ) {
        return ResponseEntity.ok(submissionService.getSubmissionById(submissionId));
    }

    @PostMapping("/fetchAll")
    public ResponseEntity<List<SubmissionCardResponseDto>> getAllSubmissions(
            @Valid @RequestBody GetAllSubmissionsRequestDto request
    ) {
        return ResponseEntity.ok().body(submissionService.getAllSubmissions(request.getSubmissionIds()));
    }

    @PutMapping("/{submission_id}")
    public ResponseEntity<?> editSubmission(
            @NotNull(message = "Submission Id should be not null")
            @PathVariable(name = "submission_id")
            Integer submissionId,
            @Valid @RequestBody  SaveSubmissionRequestDto request
    ){
        var response = submissionService.updateSubmission(submissionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    @GetMapping("/top")
    public ResponseEntity<CursorPage<SubmissionCardResponseDto>> getTopSubmissions(
            @RequestParam(required = false)  Integer cursorLike,
            @RequestParam(required = false) Instant cursorCreatedAt,
            @RequestParam(required = false) Integer cursorId,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        LocalDateTime cursorLdt = cursorCreatedAt != null
                ? LocalDateTime.ofInstant(cursorCreatedAt, ZoneId.of("UTC"))
                : null;
        CursorPage<SubmissionCardResponseDto> response = submissionService.getTopSubmission(cursorLike, cursorLdt, cursorId, limit);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/autocomplete")
    public ResponseEntity<List<AutoCompleteResponseDto>> getAutoComplete(
            @Valid @RequestBody AutocompleteRequestDto request
    ) {
        return ResponseEntity.ok().body(trieService.searchMetaData(request.query));
    }
}