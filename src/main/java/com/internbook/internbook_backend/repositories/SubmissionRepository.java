package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
}