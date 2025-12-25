package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}