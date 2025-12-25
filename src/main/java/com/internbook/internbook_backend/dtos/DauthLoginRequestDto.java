package com.internbook.internbook_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DauthLoginRequestDto {
    @NotBlank(message = "Authorization code is required.")
    private String authorizationCode;
}
