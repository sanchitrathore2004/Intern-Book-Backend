package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Integer> {

}
