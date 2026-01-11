package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.dtos.temporary.SaveDepartmentRequestDto;
import com.internbook.internbook_backend.dtos.temporary.SaveSubmissionsRequestDto;
import com.internbook.internbook_backend.dtos.temporary.SaveUserRequestDto;
import com.internbook.internbook_backend.entities.*;
import com.internbook.internbook_backend.enums.SubmissionType;
import com.internbook.internbook_backend.events.SubmissionCreatedEvent;
import com.internbook.internbook_backend.events.SubmissionDeletedEvent;
import com.internbook.internbook_backend.mappers.*;
import com.internbook.internbook_backend.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.*;

@Service
@AllArgsConstructor
public class SubmissionService {
    private final SubmissionMapper submissionMapper;
    private final ProcessStructureMapper processStructureMapper;
    private final CompanyRepository companyRepository;
    private final SubmissionRepository submissionRepository;
    private final AuthService authService;
    private final DomainMapper domainMapper;
    private final CtcMapper ctcMapper;

    private final CompanyMapper companyMapper;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final DomainRepository domainRepository;

    @Transactional
    public SubmissionResponseDto saveSubmission(SaveSubmissionRequestDto request) {
        var user = authService.getCurrentUser();
        if (user == null)
            throw new UsernameNotFoundException("Username not found");

        var domain = domainRepository.getReferenceById(request.getDomainId());
        var company = companyRepository.getReferenceById(request.getCompanyId());

        var submission = submissionMapper.toEntity(request);
        submission.setUser(user);
        submission.setDomain(domain);
        submission.setCompany(company);
        submission.setDomainDescription(request.getDomainDescription());

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

        eventPublisher.publishEvent(
                new SubmissionCreatedEvent(submission.getId())
        );
        return  toSubmissionResponseDto(submission);
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

    public void saveRole(SaveDomainRequestDto request) {
        var domain = new Domain();
        domain.setName(request.getDomainName());
        domainRepository.save(domain);
        domainMapper.toDto(domain);
    }

    public TrieSubmissionDto toTrieSubmissionDto(Submission sub) {
        var dto = new TrieSubmissionDto();

        dto.setId(sub.getId());
        dto.setCgpa(sub.getCgpa());
        dto.setExperience(sub.getExperience());
        dto.setCreatedAt(sub.getCreatedAt());
        dto.setType(sub.getType());
        dto.setResources(sub.getResources());
        dto.setClubPositions(sub.getClubPositions());
        dto.setIsFraud(sub.getIsFraud());
        dto.setIsOffensive(sub.getIsOffensive());
        dto.setLikeCount(sub.getLikeCount());
        dto.setDomainDescription(sub.getDomainDescription());

        var user = sub.getUser();
        dto.setAuthorId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProfileUrl(user.getProfileUrl());
        dto.setYear(user.getYear());
        dto.setDegree(user.getDegree());
        dto.setIsAnonymous(user.isAnonymous());

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
        dto.setDomain(domainMapper.toDto(sub.getDomain()));
        dto.setProcessStructure(processStructureMapper.toDto(sub.getProcessStructure()));

        return dto;
    }

    public SubmissionResponseDto toSubmissionResponseDto(Submission sub) {
        var dto = toTrieSubmissionDto(sub);
        var submissionDto = submissionMapper.toDto(dto);
        var currentUser = authService.getCurrentUser();

        submissionDto.setFavourite(
                currentUser.getFavouriteSubmissions().contains(sub)
        );

        return submissionDto;
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

        submission.setExperience(request.getExperience());
        submission.setResources(request.getResources());
        submission.setCgpa(request.getCgpa());
        submission.setClubPositions(request.getClubPositions());
        submission.setDomain(domainRepository.getReferenceById(request.getDomainId()));
        submission.setDomainDescription(request.getDomainDescription());

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
        Set<Integer> favouriteIds =
                submissionRepository.findFavouriteSubmissionIds(
                        authService.getCurrentUserId()
                );

        var rows = submissionRepository.findAllSubmissionsOfCompany(company.getId());

        return rows.stream()
                .map(row -> projectionToCardDto(row, favouriteIds))
                .toList();
    }

    @Transactional
    public void deleteSubmission(Integer submissionId) {
        Submission submission = validateUserSubmission(submissionId);
        var trieSubmissionDto = toTrieSubmissionDto(submission);
        submissionRepository.delete(submission);

        eventPublisher.publishEvent(
                new SubmissionDeletedEvent(trieSubmissionDto)
        );
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

    public List<SubmissionCardResponseDto> getAllSubmissions(List<Integer> submissionIds) {
        var userId = authService.getCurrentUserId();

        Set<Integer> favouriteIds =
                submissionRepository.findFavouriteSubmissionIds(userId);

        List<SubmissionCardProjection> rows =
                submissionRepository.findAllSubmissionCards(submissionIds);

        return rows.stream()
                .map(row -> projectionToCardDto(row, favouriteIds))
                .toList();
    }

    public SubmissionCardResponseDto projectionToCardDto(
            SubmissionCardProjection p,
            Set<Integer> favouriteIds
    ) {
        SubmissionCardResponseDto dto = new SubmissionCardResponseDto();

        dto.setId(p.getId());
        dto.setCompanyName(p.getCompanyName());
        dto.setCompanyLogoUrl(p.getCompanyLogoUrl());
        dto.setDomainName(p.getDomainName());
        dto.setUserName(p.getUserName());
        dto.setYear(p.getUserYear());
        dto.setSubmissionType(p.getSubmissionType());
        dto.setLikeCount(p.getLikeCount());
        dto.setFavourite(favouriteIds.contains(p.getId()));

        dto.setTags(buildTags(p));

        return dto;
    }

    private List<String> buildTags(SubmissionCardProjection p) {
        List<String> tags = new ArrayList<>();

        if (p.getHasCtc()) tags.add("CTC");
        if (p.getHasStipend()) tags.add("Stipend");
        if (p.getHasProcessStructure()) tags.add("Schedule");
        if (p.getHasExperience()) tags.add("Experience");
        if (p.getHasMistakes()) tags.add("Mistakes");
        if (p.getHasBenefits()) tags.add("Benefits");
        if (p.getHasResources()) tags.add("Resources");

        return tags;
    }

    public List<SubmissionCardResponseDto> getTopSubmission () {
        var user = authService.getCurrentUser();
        Integer companyId = user.getFavouriteCompany() != null
                ? user.getFavouriteCompany().getId()
                : null;

        List<SubmissionCardProjection> rows =
                submissionRepository.findTopSubmissions(
                        user.getDepartment().getId(),
                        companyId
                );

        Set<Integer> favouriteIds =
                submissionRepository.findFavouriteSubmissionIds(user.getId());

        return rows.stream()
                .map(row -> projectionToCardDto(row, favouriteIds))
                .toList();
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
        var domain = domainRepository.getReferenceById(request.getDomainId());
        var company = companyRepository.getReferenceById(request.getCompanyId());

        var submission = submissionMapper.toEntity(request);
        submission.setUser(user);
        submission.setDomain(domain);
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
}
