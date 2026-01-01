package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.Degree;
import com.internbook.internbook_backend.enums.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class GetUserResponseDto {
    private UserDto user;
    private int favouritesCount;
    private int submissionsCount;
    private List<FavouriteShortResponseDto> favourites;
}
