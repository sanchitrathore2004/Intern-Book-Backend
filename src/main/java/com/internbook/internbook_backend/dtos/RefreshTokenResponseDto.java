package com.internbook.internbook_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenResponseDto {
    private String accessToken;
}
