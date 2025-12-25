package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.config.JwtConfig;
import com.internbook.internbook_backend.dtos.UserDto;
import com.internbook.internbook_backend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public String generateAccessToken(UserDto userDto) {
        return generateToken(userDto, jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(UserDto userDto) {
        return generateToken(userDto, jwtConfig.getRefreshTokenExpiration());
    }

    private String generateToken(UserDto userDto, long tokenExpiration) {
        return Jwts.builder()
                .subject(String.valueOf(userDto.getId()))
                .claim("email", userDto.getEmail())
                .claim("name", userDto.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (1000 * tokenExpiration)))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public boolean validateToken (String token) {
        try {
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        }
        catch (JwtException ex) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Integer getIdFromToken (String token) {
        return Integer.valueOf(getClaims(token).getSubject());
    }
}
