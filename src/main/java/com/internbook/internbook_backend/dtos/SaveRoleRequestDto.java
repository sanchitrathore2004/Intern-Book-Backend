package com.internbook.internbook_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveRoleRequestDto {
    @Positive
    private Integer id;

    @NotBlank(message = "role name is required")
    @Size(max = 255)
    private String roleName;

    @Size(max = 5000)
    private String description;
}
