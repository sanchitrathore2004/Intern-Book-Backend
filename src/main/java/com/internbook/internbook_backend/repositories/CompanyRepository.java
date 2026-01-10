package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Company;
import com.internbook.internbook_backend.projections.CompanyStatisticsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Query("""
    SELECT
        c.id as companyId,
        AVG(DISTINCT cv.ctc) as avgCtc,
        AVG(DISTINCT cv.basePay) as avgBasePay,
        AVG(DISTINCT s.stipend) as avgStipend,
        COUNT(DISTINCT u.id) as favouriteCount
    FROM Company c
    LEFT JOIN c.ctcValues cv
    LEFT JOIN c.stipends s
    LEFT JOIN User u ON u.favouriteCompany = c
    WHERE c.id = :companyId
    GROUP BY c.id
    """)
    CompanyStatisticsProjection getCompanyStatistics(@Param("companyId") Integer companyId);

    @Query("""
    SELECT DISTINCT c FROM Company c
    LEFT JOIN FETCH c.submissions s
    LEFT JOIN FETCH s.user u
    LEFT JOIN FETCH u.department
    LEFT JOIN FETCH s.domain
    LEFT JOIN FETCH s.ctcValue
    LEFT JOIN FETCH s.stipend
    LEFT JOIN FETCH s.processStructure
    WHERE c.id = :companyId
""")
    Optional<Company> findCompanyWithSubmissions(@Param("companyId") Integer companyId);
}