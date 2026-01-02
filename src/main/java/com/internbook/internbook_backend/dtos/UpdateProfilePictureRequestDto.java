package com.internbook.internbook_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfilePictureRequestDto {
    @NotBlank(message = "Profile URL must be present")
    private String profileUrl;
}
