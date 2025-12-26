package com.internbook.internbook_backend.controllers;


import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.mappers.UserMapper;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import com.internbook.internbook_backend.repositories.UserRepository;
import com.internbook.internbook_backend.services.AuthService;
import com.internbook.internbook_backend.services.SubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @GetMapping
    private ResponseEntity<?> test(){
        var user = authService.getCurrentUser();
        return ResponseEntity.ok(userMapper.toDto(user));
    }


    @GetMapping("/favourites")
    public ResponseEntity<List<SubmissionDetailsDto>> getFavouritesByUserId() {
        User user = authService.getCurrentUser();
        Long userId = user.getId();
        List<Submission> favouriteSubmissions = submissionRepository.findByUserId(userId);
        List<SubmissionDetailsDto> response = favouriteSubmissions.stream()
                .map(submissionService::toSubmissionDetailsDto)
                .toList();
        return ResponseEntity.ok(response);
    }


    @PostMapping("/addToFavourites")
    private ResponseEntity<?> addToFavourites(
            @RequestBody SubmissionRequestDto request
    ){
        var user = authService.getCurrentUser();
        Submission submission = submissionRepository.getReferenceById(request.getSubmissionId());
        user.getFavouriteSubmissions().add(submission);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeFromFavourites")
    private ResponseEntity<?> removeFromFavourites(
            @RequestBody SubmissionRequestDto request
    ){
        User user = authService.getCurrentUser();
        Submission submission = submissionRepository.getReferenceById(request.getSubmissionId());
        user.getFavouriteSubmissions().remove(submission);
        userRepository.save(user);
        return  ResponseEntity.ok().build();
    }

}
