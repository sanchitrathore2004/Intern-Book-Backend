package com.internbook.internbook_backend.projections;

import java.math.BigDecimal;

public interface CompanyStatisticsProjection {
    Integer getCompanyId();
    BigDecimal getAvgCtc();
    BigDecimal getAvgBasePay();
    BigDecimal getAvgStipend();
    Integer getFavouriteCount();
}
