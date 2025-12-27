package com.internbook.internbook_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleResponseDto {
    private Integer id;
    private String name;
    private String description;
}
