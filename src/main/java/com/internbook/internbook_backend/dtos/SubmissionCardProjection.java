package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.SubmissionType;

public interface SubmissionCardProjection {
    Integer getId();
    String getCompanyName();
    String getCompanyLogoUrl();
    String getRoleName();
    String getUserName();
    Integer getUserYear();
    SubmissionType getSubmissionType();
    Integer getLikeCount();

    Boolean getHasCtc();
    Boolean getHasStipend();
    Boolean getHasProcessStructure();
    Boolean getHasExperience();
    Boolean getHasMistakes();
    Boolean getHasBenefits();
    Boolean getHasResources();
}
