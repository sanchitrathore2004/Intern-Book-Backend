package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByNameLike(String name);
}
