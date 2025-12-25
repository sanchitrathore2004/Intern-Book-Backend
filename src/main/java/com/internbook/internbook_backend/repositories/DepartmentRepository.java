package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Object findByName(String name);
}
