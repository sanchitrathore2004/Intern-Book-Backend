package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @EntityGraph(attributePaths = {"department"})
    Optional<User> findOneById(Long id);
}