package com.internbook.internbook_backend.dtos;

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
    private Integer companyId;
    private String companyName;
    private String userName;
    private Long userId;
    private SubmissionType submissionType;
    private String domainName;
    private Integer year;
    private List<String> tags;
    private String companyLogoUrl;
    private boolean isFavourite;
    private Integer likeCount;
    private boolean isAnonymous;
}
