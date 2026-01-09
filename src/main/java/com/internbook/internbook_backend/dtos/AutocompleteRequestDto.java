package com.internbook.internbook_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AutocompleteRequestDto {
    @NotBlank
    public String query;
}
