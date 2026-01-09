package com.internbook.internbook_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AutoCompleteResponseDto {
    private String word;
    private List<Integer> ids;
}
