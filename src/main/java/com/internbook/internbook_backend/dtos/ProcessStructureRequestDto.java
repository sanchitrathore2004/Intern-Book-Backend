package com.internbook.internbook_backend.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProcessStructureRequestDto {
    private Integer otRounds;
    private Integer piRounds;
    private LocalDate startDate;
    private LocalDate endDate;
}
