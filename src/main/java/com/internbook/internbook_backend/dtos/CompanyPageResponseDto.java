package com.internbook.internbook_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyPageResponseDto {
    private Integer id;
    private String name;
    private String logoUrl;
    private String description;
    private BigDecimal averageCtc;
    private BigDecimal averageStipend;
    private BigDecimal averageBase;
    private Integer favoriteCount;
    private List<SubmissionResponseDto> submission;
}
