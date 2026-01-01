package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.entities.Role;
import com.internbook.internbook_backend.enums.SubmissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionCardResponseDto {
    private Integer id;
    private String companyName;
    private String userName;
    private SubmissionType submissionType;
    private String role;
    private Integer year;
    private List<String> tags;
    private String companyLogoUrl;
    private boolean isFavourite;
}
