package com.internbook.internbook_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCompanyRequestDto {
    @NotNull
    @NotBlank
    private String companyName;
}
