package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Stipend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StipendRepository extends JpaRepository<Stipend, Integer> {
}