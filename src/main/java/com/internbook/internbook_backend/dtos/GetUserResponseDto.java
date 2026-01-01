package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.Degree;
import com.internbook.internbook_backend.enums.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class GetUserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String department;
    private Integer year;
    private Degree degree;
    private UserRole userRole;
    private boolean isAnonymous;
    private String profileUrl;
    private int favouritesCount;
    private int submissionsCount;
    private List<FavouriteShortResponseDto> favourites;
}
