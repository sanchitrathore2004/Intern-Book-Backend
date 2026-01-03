package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.mappers.UserMapper;
import com.internbook.internbook_backend.mappers.CompanyMapper;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import com.internbook.internbook_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final AuthService authService;
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;

    public UserProfileResponseDto getProfileById(Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if(user == null)
            throw new UsernameNotFoundException("User not found");

        var submissions = submissionRepository.findAllByUserId(user.getId());

        Set<Integer> favouriteSubmissionIds =
                submissionRepository.findFavouriteSubmissionIds(user.getId());

        List<SubmissionCardResponseDto> mySubmissions =
                submissions.stream()
                        .map(submission ->
                                submissionService.submissionToSubmissionCardResponseDto(
                                        submission,
                                        favouriteSubmissionIds
                                )
                        )
                        .toList();

        var profileDto = new UserProfileResponseDto(user);
        profileDto.setMySubmissions(mySubmissions);
        profileDto.setFavouriteCompany(companyMapper.toDto(user.getFavouriteCompany()));
        return profileDto;
    }

    public User updateProfile(UpdateProfileRequestDto request) {
        var user = authService.getCurrentUser();
        user.setUserRole(request.getRole());
        user.setProfileUrl(request.getProfileUrl());
        user.setAnonymous(request.getIsAnonymous());
        userRepository.save(user);
        return user;
    }

    @Transactional
    public ToggleResponseDto toggleFavourites(Integer submissionId) {
        Long userId = authService.getCurrentUserId();
        var response = new ToggleResponseDto();

        int deleted = userRepository.removeFavourite(userId, submissionId);

        if (deleted > 0) {
            response.setFavourite(false);
        } else {
            userRepository.addFavourite(userId, submissionId);
            response.setFavourite(true);
        }

        response.setFavouritesCount((int) userRepository.countFavouritesByUserId(userId));
        response.setSubmissionsCount(submissionRepository.countByUserId(userId));

        response.setFavourites(
                userRepository.findTop3Favourites(
                        userId,
                        PageRequest.of(0, 3)
                )
        );
        return response;
    }

    @Transactional
    public void deleteFromFavourite(Integer submissionId) {
        userRepository.removeFavourite(authService.getCurrentUser().getId(), submissionId);
    }

    @Transactional
    public void addToFavourite(Integer submissionId) {
        userRepository.addFavourite(authService.getCurrentUser().getId(), submissionId);
    }

    public List<SubmissionCardResponseDto> getFavouriteSubmissions() {
        User user = authService.getCurrentUser();
        List<Submission> favouriteSubmissions = submissionRepository.findFavouriteSubmissions(user.getId());
        return favouriteSubmissions.stream()
                .map(submission -> submissionService.submissionToSubmissionCardResponseDto(
                        submission,
                        submissionRepository.findFavouriteSubmissionIds(user.getId()
                        )
                ))
                .toList();
    }

    public GetUserResponseDto getUser() {
        var user = authService.getCurrentUser();

        GetUserResponseDto dto = new GetUserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setDegree(user.getDegree());
        dto.setProfileUrl(user.getProfileUrl());
        dto.setAnonymous(user.isAnonymous());
        dto.setDepartment(user.getDepartment().getName());
        dto.setUserRole(user.getUserRole());
        dto.setYear(user.getYear());
        dto.setFavouritesCount((int) userRepository.countFavouritesByUserId(user.getId()));
        dto.setSubmissionsCount(submissionRepository.countByUserId(user.getId()));

        dto.setFavourites(
                userRepository.findTop3Favourites(
                        user.getId(),
                        PageRequest.of(0, 3)
                )
        );
        return dto;
    }

}

