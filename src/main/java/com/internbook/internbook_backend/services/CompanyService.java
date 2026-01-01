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
    private final AuthService authService;
    private final SubmissionRepository submissionRepository;

    public CompanyPageResponseDto getCompany(Integer companyId) {
        var company = companyRepository
                .findCompanyWithSubmissions(companyId)
                .orElseThrow(CompanyNotFoundException::new);
        List<SubmissionCardResponseDto> submissionCardResponseDtos = generateSubmissionCardResponseDtos(company);
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

    private List<SubmissionCardResponseDto> generateSubmissionCardResponseDtos(Company company) {
        Set<Integer> favouriteSubmissionIds =
                submissionRepository.findFavouriteSubmissionIds(
                        authService.getCurrentUser().getId()
                );
        return company.getSubmissions()
                .stream()
                .map(submission -> submissionToSubmissionCardResponseDto(
                        submission,
                        favouriteSubmissionIds
                ))
                .toList();
    }


    private SubmissionCardResponseDto submissionToSubmissionCardResponseDto(
            Submission submission,
            Set<Integer> favouriteSubmissionIds
    ) {
        SubmissionCardResponseDto submissionCardResponseDto = new SubmissionCardResponseDto();
        submissionCardResponseDto.setId(submission.getId());
        submissionCardResponseDto.setCompanyName(submission.getCompany().getName());
        submissionCardResponseDto.setUserName(submission.getUser().getName());
        submissionCardResponseDto.setSubmissionType(submission.getType());
        submissionCardResponseDto.setRole(submission.getRole().getName());
        submissionCardResponseDto.setYear(submission.getUser().getYear());
        submissionCardResponseDto.setCompanyLogoUrl(submission.getCompany().getLogoUrl());
        submissionCardResponseDto.setTags(generateTags(submission));
        submissionCardResponseDto.setFavourite(
                favouriteSubmissionIds.contains(submission.getId())
        );
        return submissionCardResponseDto;
    }

    private List<String> generateTags(Submission submission) {
        return Stream.of(
                        entry(submission.getCtcValue(), "CTC"),
                        entry(submission.getStipend(), "Stipend"),
                        entry(submission.getProcessStructure(), "Schedule"),
                        entry(submission.getExperience(), "Experience"),
                        entry(submission.getMistakes(), "Mistakes"),
                        entry(submission.getBenefits(), "Benefits"),
                        entry(submission.getResources(), "Resources")
                )
                .filter(e -> e.getKey() != null && !(e.getKey() instanceof Collection<?> c && c.isEmpty()))
                .map(Map.Entry::getValue)
                .toList();
    }

    private Map.Entry<Object, String> entry(Object value, String tag) {
        return Map.entry(value, tag);
    }
}
