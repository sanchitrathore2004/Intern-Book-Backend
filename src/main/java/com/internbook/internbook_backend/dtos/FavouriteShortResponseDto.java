package com.internbook.internbook_backend.dtos;

import lombok.Data;

@Data
public class FavouriteShortResponseDto {
    private int submissionId;
    private int companyId;
    private String companyName;
    private String authorName;

    public FavouriteShortResponseDto(Integer submissionId, Integer companyId, String companyName, String authorName) {
        this.submissionId = submissionId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.authorName = authorName;
    }
}
