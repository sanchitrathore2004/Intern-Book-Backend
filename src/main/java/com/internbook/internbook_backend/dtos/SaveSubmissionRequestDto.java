package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.SubmissionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaveSubmissionRequestDto {
//    @Positive
//    private Integer id;

    @NotNull(message = "Company ID is required")
    @Positive
    private Integer companyId;

    @NotNull(message = "Role ID is required")
    @Positive
    private Integer roleId;

    @NotNull(message = "Submission type is required and it must be (INTERN, PLACED or INTER_TO_PPO)")
    private SubmissionType type;

    @Size(max = 2000, message = "Benefits description is too long")
    private String benefits;

    @NotBlank(message = "Experience cannot be empty")
    @Size(min = 50, max = 20000, message = "Experience should be between 50 and 20,000 characters")
    private String experience;

    @Size(max = 5000)
    private String mistakes;

    @Size(max = 2000)
    private String resources;

    @DecimalMin(value = "0.0", message = "CGPA cannot be negative")
    @DecimalMax(value = "10.0", message = "CGPA cannot exceed 10.0")
    private BigDecimal cgpa;

    @Size(max = 1000)
    private String clubPositions;

//    ctc_values table values
    @DecimalMin(value = "0.0", message = "CTC cannot be negative")
    private BigDecimal ctc;

    @DecimalMin(value = "0.0", message = "Base pay cannot be negative")
    private BigDecimal basePay;

//    stipends table values
    @DecimalMin(value = "0.0", message = "Stipend cannot be negative")
    private BigDecimal stipendValue;

//    process_structure table values
    @Positive(message = "Ot-rounds cannot be negative")
    private Integer otRounds;

    @Positive(message = "Pi-rounds cannot be negative")
    private Integer piRounds;

    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @PastOrPresent(message = "End date cannot be in the future")
    private LocalDate endDate;
}
