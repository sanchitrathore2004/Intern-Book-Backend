package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.entities.Submission;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProcessStructureResponseDto {
    private Integer otRound;
    private Integer piRound;
    private LocalDate startDate;
    private LocalDate endDate;
}
