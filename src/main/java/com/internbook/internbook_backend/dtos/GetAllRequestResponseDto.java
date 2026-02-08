package com.internbook.internbook_backend.dtos;

import lombok.Data;

@Data
public class GetAllRequestResponseDto {
    private Integer id;
    private String userName;
    private Long userId;
    private String companyName;
}
