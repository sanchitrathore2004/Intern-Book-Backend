package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.DomainResponseDto;
import com.internbook.internbook_backend.entities.Domain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomainMapper {
    DomainResponseDto toDto(Domain domain);
}
