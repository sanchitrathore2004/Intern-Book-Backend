package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.SaveCompanyRequestDto;
import com.internbook.internbook_backend.dtos.SaveRoleRequestDto;
import com.internbook.internbook_backend.dtos.SaveSubmissionDto;
import com.internbook.internbook_backend.entities.*;
import com.internbook.internbook_backend.mappers.ProcessStructureMapper;
import com.internbook.internbook_backend.mappers.SubmissionMapper;
import com.internbook.internbook_backend.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubmissionService {
    private final SubmissionMapper submissionMapper;
    private final ProcessStructureMapper processStructureMapper;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final SubmissionRepository submissionRepository;
    private final AuthService authService;


    @Transactional
    public String saveSubmission(SaveSubmissionDto request) {
        var user = authService.getCurrentUser();
        if(user == null)
            throw new UsernameNotFoundException("Username not found");

        var role = roleRepository.getReferenceById(request.getRoleId());
        var company = companyRepository.getReferenceById(request.getCompanyId());

        var submission = submissionMapper.toEntity(request);
        submission.setUser(user);
        submission.setRole(role);
        submission.setCompany(company);

        if(request.getStipendValue() != null) {
            var stipend = new Stipend(user, company, request.getStipendValue(), submission);
            System.out.println(stipend.getStipend());
            submission.setStipend(stipend);
        }

        if(request.getCtc() != null) {
            var ctcValue = new CtcValue(user, company, request.getCtc(), submission);
            if(request.getBasePay() != null)
                ctcValue.setBasePay(request.getBasePay());

            submission.setCtcValue(ctcValue);
        }

        if(!(
                request.getPiRounds() == null ||
                        request.getOtRounds() == null ||
                        request.getStartDate() == null ||
                        request.getEndDate() == null
        )) {
            var processStructureData = processStructureMapper.toDto(request);
            var processStructure = new ProcessStructure(processStructureData);
            processStructure.setSubmissions(submission);

            submission.setProcessStructure(processStructure);
        }

        submissionRepository.save(submission);
        return "Submission saved successfully";
    }

    public String saveCompany(SaveCompanyRequestDto request) {
        var company = new Company();
        company.setName(request.getCompanyName());
        if(request.getLogoUrl() != null)
            company.setLogoUrl(request.getLogoUrl());

        companyRepository.save(company);
        return "Company saved successfully!";
    }

    public String saveRole(SaveRoleRequestDto request) {
        var role = new Role();
        role.setName(request.getRoleName());
        if(request.getDescription() != null)
            role.setDescription(request.getDescription());

        roleRepository.save(role);
        return "Role saved successfully!";
    }
}
