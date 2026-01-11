package com.internbook.internbook_backend.dtos.temporary;

import com.internbook.internbook_backend.enums.SubmissionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaveSubmissionsRequestDto {
    private Integer id;
    private Long userId;
    private Integer companyId;
    private Integer domainId;
    private SubmissionType type;
    private String experience;
    private String domainDescription;
    private String resources;
    private BigDecimal cgpa;
    private String clubPositions;
    private BigDecimal ctc;
    private BigDecimal basePay;
    private BigDecimal stipendValue;
    private Integer otRounds;
    private Integer piRounds;
    private LocalDate startDate;
    private LocalDate endDate;
}
