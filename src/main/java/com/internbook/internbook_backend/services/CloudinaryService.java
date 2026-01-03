package com.internbook.internbook_backend.services;

import com.cloudinary.Cloudinary;
import com.internbook.internbook_backend.dtos.CloudinaryResponseDto;
import com.internbook.internbook_backend.exceptions.CloudinarySignatureException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryService {
    private final AuthService authService;
    private final Cloudinary cloudinary;

    public CloudinaryResponseDto getCloudinaryResponse () {
        try {
            var user = authService.getCurrentUser();
            Map<String, Object> params = new HashMap<>();
            long timeStamp = System.currentTimeMillis() / 1000;
            params.put("timestamp", timeStamp);
            params.put("folder", "users/profile/" + user.getId());
            String signature = cloudinary.apiSignRequest(params, cloudinary.config.apiSecret);
            CloudinaryResponseDto cloudinaryResponseDto = new CloudinaryResponseDto();
            cloudinaryResponseDto.setSignature(signature);
            cloudinaryResponseDto.setFolder(params.get("folder"));
            cloudinaryResponseDto.setTimeStamp(timeStamp);
            cloudinaryResponseDto.setApiKey(cloudinary.config.apiKey);
            cloudinaryResponseDto.setCloudName(cloudinary.config.cloudName);
            return cloudinaryResponseDto;
        }
        catch (Exception e) {
            throw new CloudinarySignatureException();
        }
    }
}
