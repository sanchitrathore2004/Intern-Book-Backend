package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> dauthLogin (
            @Valid @RequestBody DauthLoginRequestDto request,
            HttpServletResponse response
    ) {
        return authService.dauthLogin(request.getAuthorizationCode(), response);
    }
}
