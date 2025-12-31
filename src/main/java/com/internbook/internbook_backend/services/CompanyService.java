package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.CompanyPageResponseDto;
import com.internbook.internbook_backend.dtos.SubmissionResponseDto;
import com.internbook.internbook_backend.entities.Company;
import com.internbook.internbook_backend.exceptions.CompanyNotFoundException;
import com.internbook.internbook_backend.projections.CompanyStatisticsProjection;
import com.internbook.internbook_backend.repositories.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final SubmissionService submissionService;

    public CompanyPageResponseDto getCompany(Integer companyId) {
        var company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            throw new CompanyNotFoundException();
        }
        List<SubmissionResponseDto> submissionResponseDtos = generateCompanySubmissionDto(company);
        CompanyStatisticsProjection statisticsProjection = companyRepository.getCompanyStatistics(companyId);
        return convertToDto(company, statisticsProjection, submissionResponseDtos);
    }

    private CompanyPageResponseDto convertToDto(Company company, CompanyStatisticsProjection statisticsProjection, List<SubmissionResponseDto> submissionResponseDtos) {
        var companyPageResponseDto = new CompanyPageResponseDto();
        companyPageResponseDto.setId(company.getId());
        companyPageResponseDto.setName(company.getName());
        companyPageResponseDto.setLogoUrl(company.getLogoUrl());
        companyPageResponseDto.setDescription(company.getDescription());
        companyPageResponseDto.setAverageCtc(statisticsProjection.getAvgCtc());
        companyPageResponseDto.setAverageBase(statisticsProjection.getAvgBasePay());
        companyPageResponseDto.setFavoriteCount(statisticsProjection.getFavouriteCount());
        companyPageResponseDto.setAverageStipend(statisticsProjection.getAvgStipend());
        companyPageResponseDto.setSubmission(submissionResponseDtos);
        return companyPageResponseDto;
    }

    private List<SubmissionResponseDto> generateCompanySubmissionDto(Company company) {
        List<SubmissionResponseDto> submissionResponseDtos  = new ArrayList<>();
        company.getSubmissions().forEach((submission) -> {
            submissionResponseDtos.add(submissionService.toSubmissionResponseDto(submission));
        });
        return submissionResponseDtos;
    }
}
