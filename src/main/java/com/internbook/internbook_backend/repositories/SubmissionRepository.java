package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.dtos.SubmissionCardProjection;
import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    @Query("""
SELECT
    s.id AS id,
    c.name AS companyName,
    c.logoUrl AS companyLogoUrl,
    r.name AS roleName,
    u.name AS userName,
    u.year AS userYear,
    s.type AS submissionType,

    COUNT(DISTINCT allFavs) AS likeCount,

    (COUNT(DISTINCT cv) > 0) AS hasCtc,
    (COUNT(DISTINCT st) > 0) AS hasStipend,
    (COUNT(DISTINCT ps) > 0) AS hasProcessStructure,

    (MAX(CASE WHEN s.experience IS NOT NULL AND s.experience <> '' THEN 1 ELSE 0 END) = 1) AS hasExperience,
    (MAX(CASE WHEN s.mistakes IS NOT NULL AND s.mistakes <> '' THEN 1 ELSE 0 END) = 1) AS hasMistakes,
    (MAX(CASE WHEN s.benefits IS NOT NULL AND s.benefits <> '' THEN 1 ELSE 0 END) = 1) AS hasBenefits,
    (MAX(CASE WHEN s.resources IS NOT NULL AND s.resources <> '' THEN 1 ELSE 0 END) = 1) AS hasResources

FROM Submission s
JOIN s.company c
JOIN s.role r
JOIN s.user u

JOIN s.favouriteBy favUser
    WITH favUser.id = :userId

LEFT JOIN s.favouriteBy allFavs

LEFT JOIN s.ctcValue cv
LEFT JOIN s.stipend st
LEFT JOIN s.processStructure ps

GROUP BY s.id, c.id, r.id, u.id
""")
    List<SubmissionCardProjection> findFavouriteSubmissions(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"company", "role", "processStructure", "ctcValue", "stipend", "user.department"})
    @Query("""
SELECT
    s.id AS id,
    c.name AS companyName,
    c.logoUrl AS companyLogoUrl,
    r.name AS roleName,
    u.name AS userName,
    u.year AS userYear,
    s.type AS submissionType,

    COUNT(fb) AS likeCount,

    (COUNT(cv) > 0) AS hasCtc,
    (COUNT(st) > 0) AS hasStipend,
    (COUNT(ps) > 0) AS hasProcessStructure,

    (MAX(CASE WHEN s.experience IS NOT NULL AND s.experience <> '' THEN 1 ELSE 0 END) = 1) AS hasExperience,
    (MAX(CASE WHEN s.mistakes IS NOT NULL AND s.mistakes <> '' THEN 1 ELSE 0 END) = 1) AS hasMistakes,
    (MAX(CASE WHEN s.benefits IS NOT NULL AND s.benefits <> '' THEN 1 ELSE 0 END) = 1) AS hasBenefits,
    (MAX(CASE WHEN s.resources IS NOT NULL AND s.resources <> '' THEN 1 ELSE 0 END) = 1) AS hasResources

FROM Submission s
JOIN s.company c
JOIN s.role r
JOIN s.user u
LEFT JOIN s.favouriteBy fb
LEFT JOIN s.ctcValue cv
LEFT JOIN s.stipend st
LEFT JOIN s.processStructure ps

WHERE u.id = :userId

GROUP BY s.id, c.id, r.id, u.id
""")
    List<SubmissionCardProjection> findAllByUserId(@Param("userId") Long userId);

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
SELECT
    s.id AS id,
    c.name AS companyName,
    c.logoUrl AS companyLogoUrl,
    r.name AS roleName,
    u.name AS userName,
    u.year AS userYear,
    s.type AS submissionType,

    COUNT(fb) AS likeCount,

    (COUNT(cv) > 0) AS hasCtc,
    (COUNT(st) > 0) AS hasStipend,
    (COUNT(ps) > 0) AS hasProcessStructure,

    (MAX(CASE WHEN s.experience IS NOT NULL AND s.experience <> '' THEN 1 ELSE 0 END) = 1) AS hasExperience,
    (MAX(CASE WHEN s.mistakes IS NOT NULL AND s.mistakes <> '' THEN 1 ELSE 0 END) = 1) AS hasMistakes,
    (MAX(CASE WHEN s.benefits IS NOT NULL AND s.benefits <> '' THEN 1 ELSE 0 END) = 1) AS hasBenefits,
    (MAX(CASE WHEN s.resources IS NOT NULL AND s.resources <> '' THEN 1 ELSE 0 END) = 1) AS hasResources

FROM Submission s
JOIN s.company c
JOIN s.role r
JOIN s.user u
LEFT JOIN s.favouriteBy fb
LEFT JOIN s.ctcValue cv
LEFT JOIN s.stipend st
LEFT JOIN s.processStructure ps

WHERE
        u.department.id = :departmentId
        OR (
            :companyId IS NOT NULL
            AND c.id = :companyId
        )

GROUP BY s.id, c.id, r.id, u.id
ORDER BY s.likeCount DESC
""")
    List<SubmissionCardProjection> findTopSubmissions(
            @Param("departmentId") Integer departmentId,
            @Param("companyId") Integer companyId
    );

    @Query("""
SELECT
    s.id AS id,
    c.name AS companyName,
    c.logoUrl AS companyLogoUrl,
    r.name AS roleName,
    u.name AS userName,
    u.year AS userYear,
    s.type AS submissionType,

    COUNT(fb) AS likeCount,

    (COUNT(cv) > 0) AS hasCtc,
    (COUNT(st) > 0) AS hasStipend,
    (COUNT(ps) > 0) AS hasProcessStructure,

    (MAX(CASE WHEN s.experience IS NOT NULL AND s.experience <> '' THEN 1 ELSE 0 END) = 1) AS hasExperience,
    (MAX(CASE WHEN s.mistakes IS NOT NULL AND s.mistakes <> '' THEN 1 ELSE 0 END) = 1) AS hasMistakes,
    (MAX(CASE WHEN s.benefits IS NOT NULL AND s.benefits <> '' THEN 1 ELSE 0 END) = 1) AS hasBenefits,
    (MAX(CASE WHEN s.resources IS NOT NULL AND s.resources <> '' THEN 1 ELSE 0 END) = 1) AS hasResources

FROM Submission s
JOIN s.company c
JOIN s.role r
JOIN s.user u
LEFT JOIN s.favouriteBy fb
LEFT JOIN s.ctcValue cv
LEFT JOIN s.stipend st
LEFT JOIN s.processStructure ps

WHERE s.id IN :submissionIds

GROUP BY s.id, c.id, r.id, u.id
""")
    List<SubmissionCardProjection> findAllSubmissionCards(
            @Param("submissionIds") List<Integer> submissionIds
    );

    @Query("""
SELECT
    s.id AS id,
    c.name AS companyName,
    c.logoUrl AS companyLogoUrl,
    r.name AS roleName,
    u.name AS userName,
    u.year AS userYear,
    s.type AS submissionType,

    COUNT(fb) AS likeCount,

    (COUNT(cv) > 0) AS hasCtc,
    (COUNT(st) > 0) AS hasStipend,
    (COUNT(ps) > 0) AS hasProcessStructure,

    (MAX(CASE WHEN s.experience IS NOT NULL AND s.experience <> '' THEN 1 ELSE 0 END) = 1) AS hasExperience,
    (MAX(CASE WHEN s.mistakes IS NOT NULL AND s.mistakes <> '' THEN 1 ELSE 0 END) = 1) AS hasMistakes,
    (MAX(CASE WHEN s.benefits IS NOT NULL AND s.benefits <> '' THEN 1 ELSE 0 END) = 1) AS hasBenefits,
    (MAX(CASE WHEN s.resources IS NOT NULL AND s.resources <> '' THEN 1 ELSE 0 END) = 1) AS hasResources

FROM Submission s
JOIN s.company c
JOIN s.role r
JOIN s.user u
LEFT JOIN s.favouriteBy fb
LEFT JOIN s.ctcValue cv
LEFT JOIN s.stipend st
LEFT JOIN s.processStructure ps

WHERE c.id = :companyId

GROUP BY s.id, c.id, r.id, u.id
""")
    List<SubmissionCardProjection> findAllSubmissionsOfCompany(@Param("companyId") Integer companyId);

    int countByUserId(Long userId);

    @Modifying
    @Query("""
    UPDATE Submission s
    SET s.likeCount = s.likeCount + 1
     WHERE s.id = :submissionId
    """)
    void incrementLike(@Param("submissionId") Integer submissionId);

    @Modifying
    @Query("""
    UPDATE Submission s
    SET s.likeCount = s.likeCount - 1
    WHERE s.id = :submissionId AND s.likeCount > 0
    """)
    void decrementLike(@Param("submissionId") Integer submissionId);

    Long user(User user);
}
