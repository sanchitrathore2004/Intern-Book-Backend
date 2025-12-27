package com.internbook.internbook_backend.dtos.temporary;

import com.internbook.internbook_backend.enums.Degree;
import lombok.Data;

@Data
public class SaveUserRequestDto {
    private Long rollNo;
    private String name;
    private String email;
    private Degree degree;
    private Integer department_id;
    private Integer year;
}