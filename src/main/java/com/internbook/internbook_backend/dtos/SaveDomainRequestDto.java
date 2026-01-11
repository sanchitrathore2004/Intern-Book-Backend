package com.internbook.internbook_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveDomainRequestDto {
    @Positive
    private Integer id;

    @NotBlank(message = "domain name is required")
    @Size(max = 255)
    private String domainName;
}
