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

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.*;
import java.util.stream.Stream;

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
    private final CompanyMapper companyMapper;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubmissionResponseDto saveSubmission(SaveSubmissionRequestDto request) {
        var user = authService.getCurrentUser();
        if (user == null)
            throw new UsernameNotFoundException("Username not found");

        var role = roleRepository.getReferenceById(request.getRoleId());
        var company = companyRepository.getReferenceById(request.getCompanyId());

        var submission = submissionMapper.toEntity(request);
        submission.setUser(user);
        submission.setRole(role);
        submission.setCompany(company);

        if (request.getStipendValue() != null && request.getType()!=SubmissionType.PLACED) {
            var stipend = new Stipend(user, company, request.getStipendValue(), submission);
            submission.setStipend(stipend);
        }

        if (request.getCtc() != null && request.getType()!=SubmissionType.INTERN) {
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
        return toSubmissionResponseDto(submission);
    }

    public CompanyResponseDto saveCompany(SaveCompanyRequestDto request) {
        var company = new Company();
        company.setName(request.getCompanyName());
        if(request.getLogoUrl() != null)
            company.setLogoUrl(request.getLogoUrl());
        if(request.getDescription() != null)
            company.setDescription(request.getDescription());

        companyRepository.save(company);
        return companyMapper.toDto(company);
    }

    public RoleResponseDto saveRole(SaveRoleRequestDto request) {
        var role = new Role();
        role.setName(request.getRoleName());
        if(request.getDescription() != null)
            role.setDescription(request.getDescription());

        roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    public SubmissionResponseDto toSubmissionResponseDto(Submission sub) {
        var dto = new SubmissionResponseDto();

        dto.setId(sub.getId());
        dto.setCgpa(sub.getCgpa());
        dto.setBenefits(sub.getBenefits());
        dto.setExperience(sub.getExperience());
        dto.setCreatedAt(sub.getCreatedAt());
        dto.setType(sub.getType());
        dto.setMistakes(sub.getMistakes());
        dto.setResources(sub.getResources());
        dto.setClubPositions(sub.getClubPositions());
        dto.setIsFraud(sub.getIsFraud());
        dto.setIsOffensive(sub.getIsOffensive());
        dto.setLikeCount(sub.getLikeCount());

        var user = sub.getUser();
        dto.setAuthorId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProfileUrl(user.getProfileUrl());
        dto.setYear(user.getYear());
        dto.setDegree(user.getDegree());

        if (user.getDepartment() != null) {
            var deptDto = new DepartmentResponseDto();
            deptDto.setId(user.getDepartment().getId());
            deptDto.setName(user.getDepartment().getName());
            dto.setDepartment(deptDto);
        }

        if ((sub.getType() == SubmissionType.PLACED || sub.getType() == SubmissionType.INTERN_TO_PPO)
                && sub.getCtcValue() != null) {
            dto.setCtc(ctcMapper.toDto(sub.getCtcValue()));
        }

        if (sub.getType() == SubmissionType.INTERN && sub.getStipend() != null) {
            dto.setStipend(sub.getStipend().getStipend());
        }

        dto.setCompany(companyMapper.toDto(sub.getCompany()));
        dto.setRole(roleMapper.toDto(sub.getRole()));
        dto.setProcessStructure(processStructureMapper.toDto(sub.getProcessStructure()));
        var currentUser = authService.getCurrentUser();
        dto.setFavourite(
                currentUser.getFavouriteSubmissions().contains(sub)
        );

        return dto;
    }

    public SubmissionResponseDto getSubmissionById(Integer id) {
        var submission = submissionRepository.findWithAllDetails(id).orElse(null);
        if(submission == null)
            throw new UsernameNotFoundException("Submission not found");

        return toSubmissionResponseDto(submission);
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


        if (companyChanged || typeChanged) {
            if(companyChanged){
                Company company = companyRepository.getReferenceById(request.getCompanyId());
                submission.setCompany(company);
            }
            if (typeChanged) {
                submission.setType(request.getType());
            }
        }
        submission.updateStipend(request.getStipendValue());
        submission.updateCtc(request.getCtc(), request.getBasePay());

        submissionRepository.save(submission);
        return toSubmissionResponseDto(submission);
    }

    public List<SubmissionCardResponseDto> generateSubmissionCardResponseDtos(Company company) {
        Set<Integer> favouriteSubmissionIds =
                submissionRepository.findFavouriteSubmissionIds(
                        authService.getCurrentUser().getId()
                );
        return company.getSubmissions()
                .stream()
                .map(submission -> submissionToSubmissionCardResponseDto(
                        submission,
                        favouriteSubmissionIds
                ))
                .toList();
    }

    public SubmissionCardResponseDto submissionToSubmissionCardResponseDto(
            Submission submission,
            Set<Integer> favouriteSubmissionIds
    ) {
        SubmissionCardResponseDto submissionCardResponseDto = new SubmissionCardResponseDto();
        submissionCardResponseDto.setId(submission.getId());
        submissionCardResponseDto.setCompanyName(submission.getCompany().getName());
        submissionCardResponseDto.setUserName(submission.getUser().getName());
        submissionCardResponseDto.setSubmissionType(submission.getType());
        submissionCardResponseDto.setRole(submission.getRole().getName());
        submissionCardResponseDto.setYear(submission.getUser().getYear());
        submissionCardResponseDto.setCompanyLogoUrl(submission.getCompany().getLogoUrl());
        submissionCardResponseDto.setTags(generateTags(submission));
        submissionCardResponseDto.setFavourite(
                favouriteSubmissionIds.contains(submission.getId())
        );
        return submissionCardResponseDto;
    }

    public List<String> generateTags(Submission submission) {
        return Stream.of(
                        entry(submission.getCtcValue(), "CTC"),
                        entry(submission.getStipend(), "Stipend"),
                        entry(submission.getProcessStructure(), "Schedule"),
                        entry(submission.getExperience(), "Experience"),
                        entry(submission.getMistakes(), "Mistakes"),
                        entry(submission.getBenefits(), "Benefits"),
                        entry(submission.getResources(), "Resources")
                )
                .filter(e -> e.getKey() != "" && !(e.getKey() instanceof Collection<?> c && c.isEmpty()))
                .map(Map.Entry::getValue)
                .toList();
    }

    private Map.Entry<Object, String> entry(Object value, String tag) {
        if (value == null) {
            value = "";
        }
        return Map.entry(value, tag);
    }


    public void deleteSubmission(Integer submissionId) {
        Submission submission = validateUserSubmission(submissionId);
        submissionRepository.delete(submission);
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

    public List<SubmissionCardResponseDto> getTopSubmission () {
        var user = authService.getCurrentUser();
        Integer companyId = user.getFavouriteCompany() != null
                ? user.getFavouriteCompany().getId()
                : null;
        List<Submission> submissions =
                submissionRepository.findTopSubmissions(
                        user.getDepartment().getId(),
                        companyId
                );
        Set<Integer> favouriteSubmissionIds =
                submissionRepository.findFavouriteSubmissionIds(user.getId());
        List<SubmissionCardResponseDto> response =
                submissions.stream()
                        .limit(10)
                        .map(submission ->
                                submissionToSubmissionCardResponseDto(
                                        submission,
                                        favouriteSubmissionIds
                                )
                        )
                        .toList();
        return response;
    }
}
