package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.ProcessStructureRequestDto;
import com.internbook.internbook_backend.dtos.SaveSubmissionDto;
import com.internbook.internbook_backend.dtos.ProcessStructureResponseDto;
import com.internbook.internbook_backend.entities.ProcessStructure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProcessStructureMapper {
    ProcessStructureResponseDto toDto(ProcessStructure processStructure);
    ProcessStructureRequestDto toDto(SaveSubmissionDto request);
}
