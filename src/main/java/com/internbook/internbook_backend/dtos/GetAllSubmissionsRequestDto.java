package com.internbook.internbook_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GetAllSubmissionsRequestDto {
    @NotNull
    List<Integer> submissionIds;
}
