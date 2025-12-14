package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.repositories.CompanyRepository;
import com.internbook.internbook_backend.repositories.RoleRepository;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import com.internbook.internbook_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public Submission save(Long rollNo, String roleName, String companyName, Submission.Type type, String benefits, String experience, String mistakes, String resources, Float cgpa, String clubPositions) {
        var user =  userRepository.findById(rollNo).orElseThrow();
        String companySearchName = "%" + companyName + "%";
        var company = companyRepository.findByNameLike(companySearchName).orElseThrow();
        String roleSearchName = "%" + roleName + "%";
        var role =  roleRepository.findByNameLike(roleSearchName).orElseThrow();

        var submission = Submission.builder()
                .type(type)
                .benefits(benefits)
                .experience(experience)
                .mistakes(mistakes)
                .resources(resources)
                .cgpa(cgpa)
                .clubPositions(clubPositions)
                .build();

        submission.addUser(user);
        submission.addCompany(company);
        submission.addRole(role);

        submissionRepository.save(submission);
        return submission;
    }
}
