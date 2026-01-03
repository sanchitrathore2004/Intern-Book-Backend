package com.internbook.internbook_backend.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CloudinaryResponseDto {
    private Long timeStamp;
    private String signature;
    private Object folder;
    private String apiKey;
    private String cloudName;
}
