package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.repositories.DepartmentRepository;
import com.internbook.internbook_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public User saveUser(Long rollNo, String name, String email, Integer year, User.Degree degree, String department) {
        var user = User.builder()
                .rollNo(rollNo)
                .name(name)
                .degree(degree)
                .email(email)
                .year(year)
                .build();

        var depart = departmentRepository.findByName(department).orElseThrow();
        user.addDepartment(depart);

        userRepository.save(user);
        return user;
    }
}
