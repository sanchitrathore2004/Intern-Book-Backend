package com.internbook.internbook_backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ToggleResponseDto {
    private boolean isFavourite;
    private int favouritesCount;
    private int submissionsCount;
    private List<FavouriteShortResponseDto> favourites;
}
