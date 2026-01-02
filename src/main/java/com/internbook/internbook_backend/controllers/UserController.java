package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.services.AuthService;
import com.internbook.internbook_backend.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;

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

    @PostMapping("/addToFavourites/{submission_id}")
    private ResponseEntity<?> addToFavourites(
            @NotNull(message = "Submission Id should not be null")
            @PathVariable(name = "submission_id")
            Integer submissionId
    ){
        userService.addToFavourite(submissionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/removeFromFavourites/{submission_id}")
    private ResponseEntity<?> removeFromFavourites(
            @NotNull(message = "Submission Id should not be null")
            @PathVariable(name = "submission_id")
            Integer submissionId
    ){
        userService.deleteFromFavourite(submissionId);
        return  ResponseEntity.ok().build();
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

    @GetMapping("/me")
    private ResponseEntity<GetUserResponseDto> getCurrentUserProfile() {
      return ResponseEntity.ok(userService.getUser());
    }
}
