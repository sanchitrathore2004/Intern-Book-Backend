package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRepository extends JpaRepository<Domain, Integer> {
}
