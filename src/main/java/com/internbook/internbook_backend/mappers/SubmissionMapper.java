package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.SaveSubmissionRequestDto;
import com.internbook.internbook_backend.dtos.temporary.SaveSubmissionsRequestDto;
import com.internbook.internbook_backend.entities.Submission;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface SubmissionMapper {
    Submission toEntity(SaveSubmissionRequestDto request);

    Submission toEntity(SaveSubmissionsRequestDto request);
}
