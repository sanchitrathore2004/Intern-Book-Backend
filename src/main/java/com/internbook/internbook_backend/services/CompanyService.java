package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.CompanyResponseDto;
import com.internbook.internbook_backend.entities.Company;
import com.internbook.internbook_backend.exceptions.CompanyNotFoundException;
import com.internbook.internbook_backend.projections.CompanyStatisticsProjection;
import com.internbook.internbook_backend.repositories.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyResponseDto getCompany(Integer companyId) {
        var company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            throw new CompanyNotFoundException();
        }
        CompanyStatisticsProjection statisticsProjection = companyRepository.getCompanyStatistics(companyId);
        return convertToDto(company, statisticsProjection);
    }

    private CompanyResponseDto convertToDto(Company company, CompanyStatisticsProjection statisticsProjection) {
        var companyResponseDto = new CompanyResponseDto();
        companyResponseDto.setId(company.getId());
        companyResponseDto.setName(company.getName());
        companyResponseDto.setLogoUrl(company.getLogoUrl());
        companyResponseDto.setDescription(company.getDescription());
        companyResponseDto.setAverageCtc(statisticsProjection.getAvgCtc());
        companyResponseDto.setAverageBase(statisticsProjection.getAvgBasePay());
        companyResponseDto.setFavoriteCount(statisticsProjection.getFavouriteCount());
        companyResponseDto.setAverageStipend(statisticsProjection.getAvgStipend());
        return  companyResponseDto;
    }
}
