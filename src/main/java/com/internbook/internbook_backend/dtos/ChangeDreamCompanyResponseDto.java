package com.internbook.internbook_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeDreamCompanyResponseDto {
    private boolean conflict;
    private boolean removed;
    private String currentCompanyName;
}
