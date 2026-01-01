package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Submission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    @Query("SELECT s FROM User u JOIN u.favouriteSubmissions s WHERE u.id = :userId")
    @EntityGraph(attributePaths = {
            "company",
            "role",
            "processStructure",
            "ctcValue",
            "stipend"
    })
    List<Submission> findFavouriteSubmissions(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"company", "role", "processStructure", "ctcValue", "stipend"})
    List<Submission> findAllByUserId(Long id);

    @Query("""
    SELECT s.id
    FROM User u
    JOIN u.favouriteSubmissions s
    WHERE u.id = :userId
""")
    Set<Integer> findFavouriteSubmissionIds(@Param("userId") Long userId);


}
