package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.CTCResponseDto;
import com.internbook.internbook_backend.entities.CtcValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtcMapper {
    CTCResponseDto toDto(CtcValue ctcValue);
}
