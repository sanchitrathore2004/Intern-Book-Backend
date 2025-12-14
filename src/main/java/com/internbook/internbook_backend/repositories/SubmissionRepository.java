package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Submission;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubmissionRepository extends CrudRepository<Submission, Integer> {
    Optional<Submission> findById(int id);
}
