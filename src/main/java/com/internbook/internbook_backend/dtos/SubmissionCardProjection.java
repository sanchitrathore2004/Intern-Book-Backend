package com.internbook.internbook_backend.dtos;

import com.internbook.internbook_backend.enums.SubmissionType;

import java.time.LocalDateTime;

public interface SubmissionCardProjection {
    Integer getId();
    Integer getCompanyId();
    String getCompanyName();
    String getCompanyLogoUrl();
    String getDomainName();
    String getUserName();
    Integer getUserYear();
    SubmissionType getSubmissionType();
    Integer getLikeCount();
    LocalDateTime getCreatedDate();

    Boolean getHasCtc();
    Boolean getHasStipend();
    Boolean getHasProcessStructure();
    Boolean getHasExperience();
    Boolean getHasResources();
    Boolean getAnonymous();
}
