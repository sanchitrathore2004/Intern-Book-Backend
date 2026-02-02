package com.internbook.internbook_backend.dtos;

import lombok.Data;

@Data
public class FavouriteShortResponseDto {
    private int submissionId;
    private int companyId;
    private String companyName;
    private String authorName;

    public FavouriteShortResponseDto(Integer submissionId, Integer companyId, String companyName, String authorName, Boolean isAnonymous) {
        this.submissionId = submissionId;
        this.companyId = companyId;
        this.companyName = companyName;
        if(isAnonymous){
            this.authorName = "Anonymous";
        }
        else this.authorName = authorName;
    }
}
