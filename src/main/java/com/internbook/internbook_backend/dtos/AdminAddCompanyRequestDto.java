package com.internbook.internbook_backend.dtos;

import lombok.Data;

@Data
public class AdminAddCompanyRequestDto {
    private String name;
    private String logoUrl;
    private String description;
}
