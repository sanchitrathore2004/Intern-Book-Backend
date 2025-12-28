package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.dtos.SubmissionResponseDto;
import com.internbook.internbook_backend.dtos.UpdateProfileRequestDto;
import com.internbook.internbook_backend.dtos.UserProfileResponseDto;
import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import com.internbook.internbook_backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final AuthService authService;
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;
    private final UserRepository userRepository;

    public UserProfileResponseDto getUserProfile() {
        var user = authService.getCurrentUser();
        var submissions = submissionRepository.findAllByUserId(user.getId());

        List<SubmissionResponseDto> mySubmissions =  submissions.stream()
                .map(submissionService::toSubmissionResponseDto)
                .toList();

        var profileDto = new UserProfileResponseDto(user);
        profileDto.setMySubmissions(mySubmissions);
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
}

