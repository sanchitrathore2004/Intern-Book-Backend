package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompanyRepository extends CrudRepository<Company, Integer> {
    Optional<Company> findByNameLike(String name);
}
