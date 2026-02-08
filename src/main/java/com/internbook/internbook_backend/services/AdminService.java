package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.AdminAddCompanyRequestDto;
import com.internbook.internbook_backend.entities.Company;
import com.internbook.internbook_backend.enums.UserRole;
import com.internbook.internbook_backend.repositories.CompanyRepository;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {
    private final AuthService authService;
    private final CompanyRepository companyRepository;
    private final SubmissionRepository submissionRepository;

    public ResponseEntity<?> addCompany (AdminAddCompanyRequestDto adminAddCompanyRequestDto) {
        var user = authService.getCurrentUser();
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        if(!user.getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(adminAddCompanyRequestDto.getName() == null ||
                adminAddCompanyRequestDto.getName().isEmpty() ||
                adminAddCompanyRequestDto.getLogoUrl() == null ||
                adminAddCompanyRequestDto.getLogoUrl().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var company = new Company();
        company.setName(adminAddCompanyRequestDto.getName());
        company.setLogoUrl(adminAddCompanyRequestDto.getLogoUrl());
        company.setDescription(adminAddCompanyRequestDto.getDescription());
        companyRepository.save(company);
        return  ResponseEntity.status(HttpStatus.CREATED).body("Company saved successfully");
    }

    public ResponseEntity<?> deleteSubmission (Integer submissionId) {
        var user = authService.getCurrentUser();
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        if(!user.getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(submissionId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        submissionRepository.deleteById(submissionId);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
