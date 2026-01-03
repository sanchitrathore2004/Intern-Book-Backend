package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.CompanyPageResponseDto;
import com.internbook.internbook_backend.dtos.CompanyResponseDto;
import com.internbook.internbook_backend.dtos.RoleResponseDto;
import com.internbook.internbook_backend.entities.Company;
import com.internbook.internbook_backend.entities.Role;
import com.internbook.internbook_backend.exceptions.CompanyNotFoundException;
import com.internbook.internbook_backend.mappers.CompanyMapper;
import com.internbook.internbook_backend.mappers.RoleMapper;
import com.internbook.internbook_backend.repositories.CompanyRepository;
import com.internbook.internbook_backend.repositories.RoleRepository;
import com.internbook.internbook_backend.services.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyPageResponseDto> getCompany(@PathVariable(name = "companyId")Integer companyId) {
        var companyResponseDto = companyService.getCompany(companyId);
        return ResponseEntity.ok(companyResponseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<CompanyResponseDto>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyResponseDto> res = companies.stream()
                .map(companyMapper::toDto).toList();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponseDto> res= roles.stream()
                .map(roleMapper::toDto).toList();
        return ResponseEntity.ok(res);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCompanyNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Company not found."));
    }
}
