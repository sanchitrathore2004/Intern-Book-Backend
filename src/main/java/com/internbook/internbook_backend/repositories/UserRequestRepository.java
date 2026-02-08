package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.dtos.GetAllRequestResponseDto;
import com.internbook.internbook_backend.entities.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Integer> {
    @Query("""
        SELECT
            u.name AS userName,
            u.id AS userId,
            ur.companyName AS companyName
        FROM UserRequest ur
        JOIN ur.user u
        """)
    List<GetAllRequestResponseDto> findAllRequests();
}