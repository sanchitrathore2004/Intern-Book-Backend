package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.Degree;
import com.internbook.internbook_backend.enums.SubmissionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TrieSubmissionDto {
    private Integer id;

    private Long authorId;
    private String name;
    private String email;
    private DepartmentResponseDto department;
    private Integer year;
    private Degree degree;
    private String profileUrl;
    private Boolean isAnonymous;

    private CompanyResponseDto company;
    private RoleResponseDto role;
    private SubmissionType type;
    private CTCResponseDto ctc;
    private ProcessStructureResponseDto processStructure;

    private BigDecimal stipend;
    private String benefits;
    private String experience;
    private String mistakes;
    private String resources;
    private BigDecimal cgpa;
    private String clubPositions;
    private LocalDateTime createdAt;

    private Boolean isFraud;
    private Boolean isOffensive;
    private Integer likeCount;
}
