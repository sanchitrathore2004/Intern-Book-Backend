package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.dtos.temporary.SaveDepartmentRequestDto;
import com.internbook.internbook_backend.dtos.temporary.SaveSubmissionsRequestDto;
import com.internbook.internbook_backend.dtos.temporary.SaveUserRequestDto;
import com.internbook.internbook_backend.services.SubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
@RequestMapping("/populate")
public class PopulateDbController {
    private final SubmissionService submissionService;

    @PostMapping("/users")
    public ResponseEntity<String> addUsers(
            @RequestBody ArrayList<SaveUserRequestDto> request
    ) {
        request.forEach(submissionService::saveUsers);
        return ResponseEntity.status(HttpStatus.CREATED).body("Users saved successfully");
    }
    @PostMapping("/roles")
    public ResponseEntity<String> addRoles(
            @RequestBody ArrayList<SaveDomainRequestDto> request
            ) {
        request.forEach(submissionService::saveRole);
        return ResponseEntity.status(HttpStatus.CREATED).body("Domains saved successfully");
    }

    @PostMapping("/companies")
    public ResponseEntity<String> addCompanies(
            @RequestBody ArrayList<SaveCompanyRequestDto> request
    ) {
        request.forEach(submissionService::saveCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body("Companies saved successfully");
    }

    @PostMapping("/departments")
    public ResponseEntity<String> addDepartments(
            @RequestBody ArrayList<SaveDepartmentRequestDto> request
    ) {
        request.forEach(submissionService::saveDepartment);
        return ResponseEntity.status(HttpStatus.CREATED).body("Departments saved successfully");
    }

    @PostMapping("/submissions")
    public ResponseEntity<String> addSubmissions(
            @RequestBody ArrayList<SaveSubmissionsRequestDto> request
    ) {
        request.forEach(submissionService::saveSubmissions);
        return ResponseEntity.status(HttpStatus.CREATED).body("Submissions saved successfully");
    }
}
