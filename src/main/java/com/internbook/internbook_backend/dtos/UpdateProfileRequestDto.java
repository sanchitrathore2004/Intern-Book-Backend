package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.UserRole;
import lombok.Data;

@Data
public class UpdateProfileRequestDto {
    private String profileUrl;
    private UserRole role;
    private Boolean isAnonymous;
}
