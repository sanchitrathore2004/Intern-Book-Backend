package com.internbook.internbook_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CTCResponseDto {
    private BigDecimal ctc;
    private BigDecimal basePay;
}
