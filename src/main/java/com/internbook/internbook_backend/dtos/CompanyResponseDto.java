package com.internbook.internbook_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDto {
    private Integer id;
    private String name;
    private String logoUrl;
    private String description;
    private BigDecimal averageCtc;
    private BigDecimal averageStipend;
    private BigDecimal averageBase;
    private Integer favoriteCount;
}
