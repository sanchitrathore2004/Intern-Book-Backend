package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.Degree;
import com.internbook.internbook_backend.enums.SubmissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionResponseDto {
    private Integer id;

    private Long authorId;
    private String name;
    private String email;
    private DepartmentResponseDto department;
    private Integer year;
    private Degree degree;
    private String profileUrl;

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
