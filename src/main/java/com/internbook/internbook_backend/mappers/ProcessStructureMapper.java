package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.ProcessStructureRequestDto;
import com.internbook.internbook_backend.dtos.SaveSubmissionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProcessStructureMapper {
    ProcessStructureRequestDto toDto(SaveSubmissionDto request);
}
