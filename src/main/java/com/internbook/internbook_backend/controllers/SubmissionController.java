package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.SaveRoleRequestDto;
import com.internbook.internbook_backend.dtos.SaveSubmissionDto;
import com.internbook.internbook_backend.dtos.SaveCompanyRequestDto;
import com.internbook.internbook_backend.entities.*;
import com.internbook.internbook_backend.repositories.*;
import com.internbook.internbook_backend.services.SubmissionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
            @Valid @RequestBody SaveSubmissionDto request
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
}
