package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.CompanyResponseDto;
import com.internbook.internbook_backend.entities.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyResponseMapper {
    CompanyResponseDto toDto(Company company);
}
