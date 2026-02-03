package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.exceptions.DauthTokenNotRetrievedException;
import com.internbook.internbook_backend.exceptions.DauthUserNotRetrievedException;
import com.internbook.internbook_backend.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

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

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken (@RequestBody ValidateTokenRequestDto request)
    {
        return authService.validate(request.getToken());
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken (@CookieValue(name = "refreshToken") String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @ExceptionHandler(DauthTokenNotRetrievedException.class)
    public ResponseEntity<Map<String, String>> handleDauthTokenNotRetrieved () {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("error", "DAuth service is down, token can not be retrieved.")
        );
    }

    @ExceptionHandler(DauthUserNotRetrievedException.class)
    public ResponseEntity<Map<String, String>>  handleDauthUserNotRetrieved () {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("error", "DAuth service is down, user can not be retrieved.")
        );
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> handleHttpClientErrorException () {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("error", "DAuth HTTP Client Error.")
        );
    }
}
