package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.dtos.temporary.SaveDepartmentRequestDto;
import com.internbook.internbook_backend.dtos.temporary.SaveSubmissionsRequestDto;
import com.internbook.internbook_backend.dtos.temporary.SaveUserRequestDto;
import com.internbook.internbook_backend.entities.*;
import com.internbook.internbook_backend.enums.SubmissionType;
import com.internbook.internbook_backend.mappers.*;
import com.internbook.internbook_backend.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class SubmissionService {
    private final SubmissionMapper submissionMapper;
    private final ProcessStructureMapper processStructureMapper;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final SubmissionRepository submissionRepository;
    private final AuthService authService;

    private final CtcMapper ctcMapper;
    private final RoleMapper roleMapper;
    private final CompanyResponseMapper companyResponseMapper;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final CtcValueRepository ctcValueRepository;
    private final StipendRepository stipendRepository;

    @Transactional
    public String saveSubmission(SaveSubmissionRequestDto request) {
        var user = authService.getCurrentUser();
        if (user == null)
            throw new UsernameNotFoundException("Username not found");

        var role = roleRepository.getReferenceById(request.getRoleId());
        var company = companyRepository.getReferenceById(request.getCompanyId());

        var submission = submissionMapper.toEntity(request);
        submission.setUser(user);
        submission.setRole(role);
        submission.setCompany(company);

        if (request.getStipendValue() != null) {
            var stipend = new Stipend(user, company, request.getStipendValue(), submission);
            submission.setStipend(stipend);
        }

        if (request.getCtc() != null) {
            var ctcValue = new CtcValue(user, company, request.getCtc(), submission);
            if (request.getBasePay() != null)
                ctcValue.setBasePay(request.getBasePay());
            submission.setCtcValue(ctcValue);
        }

        if (!(request.getPiRounds() == null ||
                request.getOtRounds() == null ||
                request.getStartDate() == null ||
                request.getEndDate() == null)) {

            var processStructureData = processStructureMapper.toDto(request);
            var processStructure = new ProcessStructure(processStructureData);
            processStructure.setSubmissions(submission);

            submission.setProcessStructure(processStructure);
        }

        submissionRepository.save(submission);
        return "Submission saved successfully";
    }

    public String saveCompany(SaveCompanyRequestDto request) {
        var company = new Company();
        company.setName(request.getCompanyName());
        if(request.getLogoUrl() != null)
            company.setLogoUrl(request.getLogoUrl());

        companyRepository.save(company);
        return "Company saved successfully!";
    }

    public String saveRole(SaveRoleRequestDto request) {
        var role = new Role();
        role.setName(request.getRoleName());
        if(request.getDescription() != null)
            role.setDescription(request.getDescription());

        roleRepository.save(role);
        return "Role saved successfully!";
    }

    public SubmissionResponseDto toSubmissionDetailsDto(Submission sub) {
        var dto = new SubmissionResponseDto();
        dto.setId(sub.getId());
        dto.setCgpa(sub.getCgpa());
        dto.setBenefits(sub.getBenefits());
        dto.setExperience(sub.getExperience());
        dto.setMistakes(sub.getMistakes());
        dto.setClubPositions(sub.getClubPositions());
        dto.setCreatedAt(sub.getCreatedAt());
        dto.setType(sub.getType());
        dto.setResources(sub.getResources());
        dto.setIsFraud(sub.getIsFraud());
        dto.setIsOffensive(sub.getIsOffensive());
        dto.setLikeCount(sub.getLikeCount());

        if ((sub.getType() == SubmissionType.PLACED ||
                sub.getType() == SubmissionType.INTERN_TO_PPO) &&
                sub.getCtcValue() != null) {
            dto.setCtc(ctcMapper.toDto(sub.getCtcValue()));
        }

        if (sub.getType() == SubmissionType.INTERN &&
                sub.getStipend() != null) {
            dto.setStipend(sub.getStipend().getStipend());
        }

        dto.setCompany(companyResponseMapper.toDto(sub.getCompany()));
        dto.setRole(roleMapper.toDto(sub.getRole()));
        dto.setProcessStructure(processStructureMapper.toDto(sub.getProcessStructure()));
        return dto;
    }

    //  BULK UPLOAD ROUTES - DELETE BEFORE PUSHING TO PROD
    public void saveDepartment(SaveDepartmentRequestDto request) {
        var department = new Department();
        department.setName(request.getDepartName());

        departmentRepository.save(department);
    }

    public void saveUsers(SaveUserRequestDto request) {
        var user = new User();
        user.setId(request.getRollNo());
        user.setName(request.getName());
        user.setDegree(request.getDegree());
        user.setEmail(request.getEmail());
        var department = departmentRepository.findById(request.getDepartment_id()).orElse(null);
        if(department == null)
            throw new UsernameNotFoundException("Department not found");
        user.setDepartment(department);
        user.setYear(request.getYear());

        userRepository.save(user);
    }

    public void saveSubmissions(SaveSubmissionsRequestDto request) {
        var user = userRepository.findById(request.getUserId()).orElse(null);
        var role = roleRepository.getReferenceById(request.getRoleId());
        var company = companyRepository.getReferenceById(request.getCompanyId());

        var submission = submissionMapper.toEntity(request);
        submission.setUser(user);
        submission.setRole(role);
        submission.setCompany(company);

        if(request.getStipendValue() != null) {
            var stipend = new Stipend(user, company, request.getStipendValue(), submission);
            submission.setStipend(stipend);
        }

        if(request.getCtc() != null) {
            var ctcValue = new CtcValue(user, company, request.getCtc(), submission);
            if(request.getBasePay() != null)
                ctcValue.setBasePay(request.getBasePay());

            submission.setCtcValue(ctcValue);
        }

        if(!(
                request.getPiRounds() == null ||
                        request.getOtRounds() == null ||
                        request.getStartDate() == null ||
                        request.getEndDate() == null
        )) {
            var processStructureData = processStructureMapper.toDto(request);
            var processStructure = new ProcessStructure(processStructureData);
            processStructure.setSubmissions(submission);

            submission.setProcessStructure(processStructure);
        }

        submissionRepository.save(submission);
    }

    @Transactional
    public SubmissionResponseDto updateSubmission(
            Integer submissionId,
            SaveSubmissionRequestDto request
    ) {

        Submission submission = validateUserSubmission(submissionId);

        submission.setBenefits(request.getBenefits());
        submission.setExperience(request.getExperience());
        submission.setMistakes(request.getMistakes());
        submission.setResources(request.getResources());
        submission.setCgpa(request.getCgpa());
        submission.setClubPositions(request.getClubPositions());
        submission.setRole(roleRepository.getReferenceById(request.getRoleId()));

        boolean companyChanged = !Objects.equals(submission.getCompany().getId(), request.getCompanyId());
        boolean typeChanged = submission.getType() != request.getType();

        if (companyChanged) {
            Company company = companyRepository.getReferenceById(request.getCompanyId());
            submission.setCompany(company);
        }

        if (typeChanged) {
            submission.setType(request.getType());
        }
        applyFinancials(submission, request, submission.getCompany());
        return toSubmissionDetailsDto(submission);
    }

    @Transactional
    public void deleteSubmission(Integer submissionId) {
        Submission submission = validateUserSubmission(submissionId);
        submissionRepository.delete(submission);
    }

    private void applyFinancials(
            Submission submission,
            SaveSubmissionRequestDto request,
            Company company
    ) {
        submission.setCtcValue(null);
        submission.setStipend(null);

        if (request.getCtc() != null && submission.getType()!=SubmissionType.INTERN) {
            CtcValue ctc = new CtcValue();
            ctc.setSubmission(submission);
            ctc.setCompany(company);
            ctc.setCtc(request.getCtc());
            ctc.setBasePay(request.getBasePay());
            ctc.setUser(submission.getUser());
            ctcValueRepository.save(ctc);
            submission.setCtcValue(ctc);
            return;
        }

        if (request.getStipendValue() != null && submission.getType()!=SubmissionType.PLACED) {
            Stipend stipend = new Stipend();
            stipend.setSubmission(submission);
            stipend.setCompany(company);
            stipend.setStipend(request.getStipendValue());
            stipend.setUser(submission.getUser());
            stipendRepository.save(stipend);
            submission.setStipend(stipend);
        }
    }

    public Submission validateUserSubmission(Integer submissionId) {
        var user = authService.getCurrentUser();

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Submission not found")
                );

        if (!submission.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User not allowed to delete submission");
        }
        return submission;
    }


}
