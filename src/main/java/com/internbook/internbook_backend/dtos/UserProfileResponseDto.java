package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.enums.Degree;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileResponseDto {
    private Long id;
    private String name;
    private String email;
    private String department;
    private Integer year;
    private Degree degree;
    private String profileUrl;

    private List<SubmissionCardResponseDto> mySubmissions;

    public UserProfileResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.degree = user.getDegree();
        this.department = user.getDepartment().getName();
        this.profileUrl = user.getProfileUrl();
        this.year = user.getYear();
    }
}
