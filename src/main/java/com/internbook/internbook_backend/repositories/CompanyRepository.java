package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.Company;
import com.internbook.internbook_backend.projections.CompanyStatisticsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Query("""
    SELECT
        c.id as companyId,
        AVG(cv.ctc) as avgCtc,
        AVG(cv.basePay) as avgBasePay,
        AVG(s.stipend) as avgStipend,
        COUNT(u.id) as favouriteCount
    FROM Company c
    LEFT JOIN c.ctcValues cv
    LEFT JOIN c.stipends s
    LEFT JOIN User u ON u.favouriteCompany = c
    WHERE c.id = :companyId
    GROUP BY c.id
""")
    CompanyStatisticsProjection getCompanyStatistics(Integer companyId);
}