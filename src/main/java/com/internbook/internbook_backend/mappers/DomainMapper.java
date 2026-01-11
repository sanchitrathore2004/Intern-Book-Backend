package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.DomainResponseDto;
import com.internbook.internbook_backend.entities.Domain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DomainMapper {
    @Mapping(target = "domainName", source = "name")
    DomainResponseDto toDto(Domain domain);
}
