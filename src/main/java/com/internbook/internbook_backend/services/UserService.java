package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.mappers.UserMapper;
import com.internbook.internbook_backend.mappers.CompanyMapper;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import com.internbook.internbook_backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
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
    private final UserMapper userMapper;

    public UserProfileResponseDto getUserProfile() {
        var user = authService.getCurrentUser();
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

    public ToggleResponseDto toggleFavourites(Integer submissionId) {
        User user = authService.getCurrentUser();
        Submission submission = submissionRepository.getReferenceById(submissionId);
        var favouriteSubmissions = user.getFavouriteSubmissions();
        var response = new ToggleResponseDto();
        if(favouriteSubmissions.contains(submission)) {
            favouriteSubmissions.remove(submission);
            response.setFavourite(false);
        }
        else {
            favouriteSubmissions.add(submission);
            response.setFavourite(true);
        }
        userRepository.save(user);
        return response;
    }

    public void deleteFromFavourite(Integer submissionId) {
        User user = authService.getCurrentUser();
        Submission submission = submissionRepository.getReferenceById(submissionId);
        user.getFavouriteSubmissions().remove(submission);
        userRepository.save(user);
    }

    public void addToFavourite(Integer submissionId) {
        var user = authService.getCurrentUser();
        Submission submission = submissionRepository.getReferenceById(submissionId);
        user.getFavouriteSubmissions().add(submission);
        userRepository.save(user);
    }

    public List<SubmissionResponseDto> getFavouriteSubmissions() {
        User user = authService.getCurrentUser();
        List<Submission> favouriteSubmissions = submissionRepository.findFavouriteSubmissions(user.getId());
        return favouriteSubmissions.stream()
                .map(submissionService::toSubmissionResponseDto)
                .toList();
    }

    public GetUserResponseDto getUser() {
        var  user = authService.getCurrentUser();
        GetUserResponseDto getUserResponseDto = new GetUserResponseDto();
        getUserResponseDto.setUser(userMapper.toDto(user));
        Set<Submission> favouriteSubmissions = user.getFavouriteSubmissions();
        getUserResponseDto.setFavouritesCount(favouriteSubmissions.size());
        getUserResponseDto.setSubmissionsCount(getSubmissionsCount(user.getId()));
        getUserResponseDto.setFavourites(getFavouriteShortResponseDtos(favouriteSubmissions));
        return getUserResponseDto;
    }

    private int getSubmissionsCount(Long userId) {
        return submissionRepository.findAllByUserId(userId).size();
    }

    private List<FavouriteShortResponseDto> getFavouriteShortResponseDtos(Set<Submission> favouriteSubmissions) {
        return favouriteSubmissions.stream()
                .sorted(Comparator.comparing(Submission::getCreatedAt).reversed())
                .limit(3)
                .map(e-> new FavouriteShortResponseDto(
                        e.getId(),
                        e.getCompany().getId(),
                        e.getCompany().getName(),
                        e.getUser().getName()
                ))
                .toList();
    }
}

