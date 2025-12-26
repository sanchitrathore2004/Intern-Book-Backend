package com.internbook.internbook_backend.mappers;

import com.internbook.internbook_backend.dtos.RoleResponseDto;
import com.internbook.internbook_backend.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponseDto toDto(Role role);
}
