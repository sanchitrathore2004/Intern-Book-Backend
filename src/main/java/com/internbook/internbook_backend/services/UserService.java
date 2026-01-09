package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.exceptions.ProfilePictureUploadException;
import com.internbook.internbook_backend.mappers.CompanyMapper;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import com.internbook.internbook_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

        var rows = submissionRepository.findAllByUserId(user.getId());

        Set<Integer> favouriteIds =
                submissionRepository.findFavouriteSubmissionIds(user.getId());

        List<SubmissionCardResponseDto> mySubmissions =
                rows.stream()
                        .map(row ->
                                submissionService.projectionToCardDto(row, favouriteIds))
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
            submissionRepository.decrementLike(submissionId);
            response.setFavourite(false);
        } else {
            userRepository.addFavourite(userId, submissionId);
            submissionRepository.incrementLike(submissionId);
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

    public List<SubmissionCardResponseDto> getFavouriteSubmissions() {

        var userId = authService.getCurrentUserId();

        Set<Integer> favouriteIds =
                submissionRepository.findFavouriteSubmissionIds(userId);

        List<SubmissionCardProjection> rows =
                submissionRepository.findFavouriteSubmissions(userId);

        return rows.stream()
                .map(row -> submissionService.projectionToCardDto(row, favouriteIds))
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

    public void updateDp (String profileUrl) {
        try {
            var user = authService.getCurrentUser();
            user.setProfileUrl(profileUrl);
            userRepository.save(user);
        } catch (Exception e) {
            throw new ProfilePictureUploadException();
        }
    }
}

