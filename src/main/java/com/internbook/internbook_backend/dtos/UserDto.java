package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.Degree;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String department;
    private Integer year;
    private Degree degree;
}
