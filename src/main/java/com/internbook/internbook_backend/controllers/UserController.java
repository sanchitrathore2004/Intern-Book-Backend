package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.exceptions.ProfilePictureUploadException;
import com.internbook.internbook_backend.repositories.UserRepository;
import com.internbook.internbook_backend.services.AuthService;
import com.internbook.internbook_backend.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/favourites")
    public ResponseEntity<List<SubmissionCardResponseDto>> getFavouritesByUserId() {
        return ResponseEntity.ok(userService.getFavouriteSubmissions());
    }

    @PutMapping("/toggleFavourites/{submission_id}")
    private ResponseEntity<ToggleResponseDto> toggleFavourites(
            @PathVariable(name="submission_id") Integer submissionId
    ) {
        var response = userService.toggleFavourites(submissionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{user_id}")
    private ResponseEntity<UserProfileResponseDto> getProfileById(
            @PathVariable(name = "user_id") Long userId
    ) {
        return ResponseEntity.ok(userService.getProfileById(userId));
    }

    @PutMapping("/updateProfile")
    private ResponseEntity<JwtResponseDto> updateUserProfile(
            @Valid @RequestBody UpdateProfileRequestDto request,
            HttpServletResponse response
    ) {
        return authService.updateProfile(userService.updateProfile(request), response);
    }

    @PutMapping("/toggleAnonymous")
    private ResponseEntity<Void> toggleAnonymous(){
        userService.toggleAnonymousProfile();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAnonymous")
    private ResponseEntity<UserAnonymousResponseDto> getAnonymousProfile() {
        return ResponseEntity.ok(userService.getAnonymousProfile());
    }

    @GetMapping("/me")
    private ResponseEntity<GetUserResponseDto> getCurrentUserProfile() {
      return ResponseEntity.ok(userService.getUser());
    }

    @PatchMapping("/update/dp")
    public ResponseEntity<Void> updateDp(@Valid @RequestBody UpdateProfilePictureRequestDto request) {
        userService.updateDp(request.getProfileUrl());
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ProfilePictureUploadException.class)
    public ResponseEntity<Map<String, String>> handleProfilePictureUploadException() {
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(
                Map.of("error", "Error uploading profile picture")
        );
    }
}
