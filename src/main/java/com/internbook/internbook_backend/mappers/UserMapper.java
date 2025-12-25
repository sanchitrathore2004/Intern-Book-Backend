package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.UserDto;
import com.internbook.internbook_backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "department", expression = "java(user.getDepartment().getName())")
    UserDto toDto (User user);
}
