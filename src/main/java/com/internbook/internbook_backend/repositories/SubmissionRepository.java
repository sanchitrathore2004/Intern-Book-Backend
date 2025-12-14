package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Submission;
import org.springframework.data.repository.CrudRepository;

public interface SubmissionRepository extends CrudRepository<Submission, Integer> {
}
