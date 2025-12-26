package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.SubmissionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaveSubmissionsRequestDto {
    private Integer id;
    private Long userId;
    private Integer companyId;
    private Integer roleId;
    private SubmissionType type;private String benefits;
    private String experience;
    private String mistakes;
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
