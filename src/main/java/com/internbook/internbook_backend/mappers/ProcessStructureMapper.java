package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.ProcessStructureRequestDto;
import com.internbook.internbook_backend.dtos.ProcessStructureResponseDto;
import com.internbook.internbook_backend.dtos.SaveSubmissionRequestDto;
import com.internbook.internbook_backend.dtos.SaveSubmissionsRequestDto;
import com.internbook.internbook_backend.entities.ProcessStructure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProcessStructureMapper {
    ProcessStructureResponseDto toDto(ProcessStructure processStructure);
    ProcessStructureRequestDto toDto(SaveSubmissionRequestDto request);
    ProcessStructureRequestDto toDto(SaveSubmissionsRequestDto request);
}
