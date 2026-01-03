package com.internbook.internbook_backend.controllers;

import com.internbook.internbook_backend.dtos.CloudinaryResponseDto;
import com.internbook.internbook_backend.exceptions.CloudinarySignatureException;
import com.internbook.internbook_backend.services.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
@AllArgsConstructor
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    @PostMapping("/signature")
    public ResponseEntity<CloudinaryResponseDto> getSignature () {
        CloudinaryResponseDto response = cloudinaryService.getCloudinaryResponse();
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(CloudinarySignatureException.class)
    public ResponseEntity<Map<String, String>> handleCloudinarySignatureException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("error", "Our cloud service is down.")
        );
    }
}
