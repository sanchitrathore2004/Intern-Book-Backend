package com.internbook.internbook_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveCompanyRequestDto {
    private Integer id;

    @NotBlank(message = "Company name cannot be empty")
    @Size(max = 255)
    private String companyName;

    @Size(max = 255)
    private String logoUrl;
}
