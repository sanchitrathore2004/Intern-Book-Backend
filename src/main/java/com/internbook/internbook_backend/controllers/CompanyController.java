package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.CompanyResponseDto;
import com.internbook.internbook_backend.exceptions.CompanyNotFoundException;
import com.internbook.internbook_backend.repositories.CompanyRepository;
import com.internbook.internbook_backend.services.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompany(@PathVariable(name = "companyId")Integer companyId) {
        var companyResponseDto = companyService.getCompany(companyId);
        return ResponseEntity.ok(companyResponseDto);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCompanyNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Company not found."));
    }
}
