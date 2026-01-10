package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.ChangeDreamCompanyResponseDto;
import com.internbook.internbook_backend.dtos.CompanyPageResponseDto;
import com.internbook.internbook_backend.dtos.CompanyResponseDto;
import com.internbook.internbook_backend.dtos.DomainResponseDto;
import com.internbook.internbook_backend.entities.Company;
import com.internbook.internbook_backend.entities.Domain;
import com.internbook.internbook_backend.exceptions.CompanyNotFoundException;
import com.internbook.internbook_backend.mappers.CompanyMapper;
import com.internbook.internbook_backend.mappers.DomainMapper;
import com.internbook.internbook_backend.repositories.CompanyRepository;
import com.internbook.internbook_backend.repositories.DomainRepository;
import com.internbook.internbook_backend.repositories.UserRepository;
import com.internbook.internbook_backend.services.AuthService;
import com.internbook.internbook_backend.services.CompanyService;
import jakarta.transaction.Transactional;
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
    private final AuthService authService;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;
    private final DomainRepository domainRepository;
    private final DomainMapper domainMapper;

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

    @GetMapping("/domains")
    public ResponseEntity<List<DomainResponseDto>> getAllRoles() {
        List<Domain> domains = domainRepository.findAll();
        List<DomainResponseDto> res= domains.stream()
                .map(domainMapper::toDto).toList();
        return ResponseEntity.ok(res);
    }

    @Transactional
    @PostMapping("/dream/{companyId}")
    public ResponseEntity<ChangeDreamCompanyResponseDto> changeDreamCompany(@PathVariable(name = "companyId")Integer companyId) {
        var company = companyRepository.findById(companyId).orElse(null);
        if(company == null)
            throw new CompanyNotFoundException();

        var user = authService.getCurrentUser();
        var favCompany = user.getFavouriteCompany();
        if(favCompany == null) {
            user.setFavouriteCompany(company);
            userRepository.save(user);
            return ResponseEntity.ok(
                    new ChangeDreamCompanyResponseDto(false, false, company.getName())
            );
        }
        else if(favCompany.equals(company)) {
            user.setFavouriteCompany(null);
            userRepository.save(user);
            return ResponseEntity.ok(
                    new  ChangeDreamCompanyResponseDto(false, true, null)
            );
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ChangeDreamCompanyResponseDto(true, false, user.getFavouriteCompany().getName())
            );
        }
    }

    @Transactional
    @PutMapping("/changeDream/{companyId}")
    public ResponseEntity<ChangeDreamCompanyResponseDto> updateDreamCompany(@PathVariable(name = "companyId")Integer companyId) {
        var company = companyRepository.findById(companyId).orElse(null);
        if(company == null)
            throw new CompanyNotFoundException();
        var user = authService.getCurrentUser();
        user.setFavouriteCompany(company);
        userRepository.save(user);

        return ResponseEntity.ok(
                new ChangeDreamCompanyResponseDto(false, false, company.getName())
        );
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCompanyNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Company not found."));
    }
}
