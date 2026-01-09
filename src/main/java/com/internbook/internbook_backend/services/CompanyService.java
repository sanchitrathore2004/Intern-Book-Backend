package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.CompanyPageResponseDto;
import com.internbook.internbook_backend.dtos.SubmissionCardResponseDto;
import com.internbook.internbook_backend.entities.Company;
import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.exceptions.CompanyNotFoundException;
import com.internbook.internbook_backend.projections.CompanyStatisticsProjection;
import com.internbook.internbook_backend.repositories.CompanyRepository;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final SubmissionService submissionService;

    public CompanyPageResponseDto getCompany(Integer companyId) {
        var company = companyRepository
                .findCompanyWithSubmissions(companyId)
                .orElseThrow(CompanyNotFoundException::new);

        List<SubmissionCardResponseDto> submissionCardResponseDtos = submissionService.generateSubmissionCardResponseDtos(company);
        CompanyStatisticsProjection statisticsProjection = companyRepository.getCompanyStatistics(companyId);
        return convertToDto(company, statisticsProjection, submissionCardResponseDtos);
    }

    private CompanyPageResponseDto convertToDto(Company company, CompanyStatisticsProjection statisticsProjection, List<SubmissionCardResponseDto> submissionCardResponseDtos) {
        var companyPageResponseDto = new CompanyPageResponseDto();
        companyPageResponseDto.setId(company.getId());
        companyPageResponseDto.setName(company.getName());
        companyPageResponseDto.setLogoUrl(company.getLogoUrl());
        companyPageResponseDto.setDescription(company.getDescription());
        companyPageResponseDto.setAverageCtc(statisticsProjection.getAvgCtc());
        companyPageResponseDto.setAverageBase(statisticsProjection.getAvgBasePay());
        companyPageResponseDto.setFavoriteCount(statisticsProjection.getFavouriteCount());
        companyPageResponseDto.setAverageStipend(statisticsProjection.getAvgStipend());
        companyPageResponseDto.setSubmission(submissionCardResponseDtos);
        return companyPageResponseDto;
    }
}
