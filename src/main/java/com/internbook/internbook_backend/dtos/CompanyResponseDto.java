package com.internbook.internbook_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyResponseDto {
    private Integer id;
    private String name;
    private String logoUrl;
}
