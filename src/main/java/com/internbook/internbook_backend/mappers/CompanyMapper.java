package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.CompanyResponseDto;
import com.internbook.internbook_backend.entities.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyResponseDto toDto(Company company);
}
