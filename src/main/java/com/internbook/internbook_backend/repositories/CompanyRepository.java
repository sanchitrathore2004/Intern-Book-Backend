package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}