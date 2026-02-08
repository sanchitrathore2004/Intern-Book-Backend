package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.AdminAddCompanyRequestDto;
import com.internbook.internbook_backend.entities.UserRequest;
import com.internbook.internbook_backend.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/admin")
@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/add/company")
    public ResponseEntity<?> addCompany (@RequestBody AdminAddCompanyRequestDto adminAddCompanyRequestDto) {
        return adminService.addCompany(adminAddCompanyRequestDto);
    }

    @DeleteMapping("/delete/submission")
    public ResponseEntity<?> deleteSubmission (@RequestParam Integer submissionId) {
        return adminService.deleteSubmission(submissionId);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<UserRequest>> getAllRequests() {
        return adminService.getAllRequests();
    }
}
