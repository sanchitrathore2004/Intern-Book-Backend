package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    @Query("""
        select distinct s
        from User u
        join u.favouriteSubmissions s
        join fetch s.company
        join fetch s.role
        where u.id = :userId
    """)
    List<Submission> findByUserId(@Param("userId") Long userId);
}
