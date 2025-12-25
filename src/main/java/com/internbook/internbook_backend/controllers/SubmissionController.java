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
    private final UserRepository userRepository;
    private final SubmissionService submissionService;

    @PostMapping("/{user_id}")
    @Transactional
    public ResponseEntity<?> saveSubmission(
            @Valid @RequestBody SaveSubmissionDto request,
            @PathVariable("user_id") Long userId
            ) {
        var response = submissionService.saveSubmission(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", response));
    }

    @PostMapping("/company/{user_id}")
    public ResponseEntity<?> saveCompany(
            @Valid @RequestBody SaveCompanyRequestDto request,
            @PathVariable("user_id") Long userId
    ) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        var response = submissionService.saveCompany(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", response));
    }

    @PostMapping("/role/{user_id}")
    public ResponseEntity<?> saveRole(
            @Valid @RequestBody SaveRoleRequestDto request,
            @PathVariable("user_id") Long userId
    ) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        var response =  submissionService.saveRole(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message", response));
    }
}
