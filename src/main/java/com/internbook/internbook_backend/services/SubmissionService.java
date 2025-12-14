package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.entities.ProcessStructure;
import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final ProcessStructureRepository processStructureRepository;

    @Transactional
    public Submission save(Long rollNo,
                           String roleName,
                           String companyName,
                           Submission.Type type,
                           String benefits,
                           String experience,
                           String mistakes,
                           String resources,
                           Float cgpa,
                           String clubPositions) {
        //  add validations
        if(rollNo == null || companyName == null || type == null || roleName == null || experience == null )
            return null;

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

    public ProcessStructure save(Submission submission,
                                 Integer otRounds,
                                 Integer piRounds,
                                 LocalDate startDate,
                                 LocalDate endDate) {
        if(submission == null || otRounds == null || piRounds == null ||  startDate == null || endDate == null)
            return null;

        var processStructure = ProcessStructure.builder()
                .otRound(otRounds)
                .piRound(piRounds)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        processStructure.addSubmission(submission);

        processStructureRepository.save(processStructure);
        return processStructure;
    }
}
