package com.internbook.internbook_backend.repositories;

import com.internbook.internbook_backend.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
