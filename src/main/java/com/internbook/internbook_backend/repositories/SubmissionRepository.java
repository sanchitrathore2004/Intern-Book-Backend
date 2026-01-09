package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Submission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
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

    @EntityGraph(attributePaths = {"company", "role", "processStructure", "ctcValue", "stipend", "user.department"})
    List<Submission> findAllByUserId(Long id);

    @Query("""
    SELECT s.id
    FROM User u
    JOIN u.favouriteSubmissions s
    WHERE u.id = :userId
""")
    Set<Integer> findFavouriteSubmissionIds(@Param("userId") Long userId);

    @Query("SELECT s FROM Submission s " +
            "JOIN FETCH s.user u " +
            "JOIN FETCH u.department d " +
            "JOIN FETCH s.role r " +
            "JOIN FETCH s.company c " +
            "LEFT JOIN FETCH s.processStructure ps " +
            "LEFT JOIN FETCH s.ctcValue cv " +
            "LEFT JOIN FETCH s.stipend st " +
            "WHERE s.id = :submissionId")
    Optional<Submission> findWithAllDetails(@Param("submissionId") Integer submissionId);

    @Query("SELECT s FROM Submission s " +
            "JOIN FETCH s.user u " +
            "JOIN FETCH u.department d " +
            "JOIN FETCH s.role r " +
            "JOIN FETCH s.company c " +
            "LEFT JOIN FETCH s.processStructure ps " +
            "LEFT JOIN FETCH s.ctcValue cv " +
            "LEFT JOIN FETCH s.stipend st ")
    List<Submission> findAllWithDetails();

    @Query("""
    SELECT DISTINCT s
    FROM Submission s
    JOIN FETCH s.user u
    JOIN FETCH s.company c
    JOIN FETCH s.role r
    LEFT JOIN FETCH s.ctcValue
    LEFT JOIN FETCH s.processStructure
    LEFT JOIN FETCH s.stipend
    WHERE
        u.department.id = :departmentId
        OR (
            :companyId IS NOT NULL
            AND c.id = :companyId
        )
    ORDER BY s.likeCount DESC
""")
    List<Submission> findTopSubmissions(
            @Param("departmentId") Integer departmentId,
            @Param("companyId") Integer companyId
    );

    int countByUserId(Long userId);

    @Modifying
    @Query("""
    UPDATE Submission s
    SET s.likeCount = s.likeCount + 1
     WHERE s.id = :submissionId
    """)
    int incrementLike(@Param("submissionId") Integer submissionId);

    @Modifying
    @Query("""
    UPDATE Submission s
    SET s.likeCount = s.likeCount - 1
    WHERE s.id = :submissionId AND s.likeCount > 0
    """)
    int decrementLike(@Param("submissionId") Integer submissionId);

}
