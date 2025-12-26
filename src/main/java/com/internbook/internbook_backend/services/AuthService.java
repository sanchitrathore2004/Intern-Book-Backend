package com.internbook.internbook_backend.services;

import com.internbook.internbook_backend.config.DepartmentConfig;
import com.internbook.internbook_backend.config.JwtConfig;
import com.internbook.internbook_backend.config.YearConfig;
import com.internbook.internbook_backend.dtos.*;
import com.internbook.internbook_backend.entities.Department;
import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.enums.Degree;
import com.internbook.internbook_backend.exceptions.DauthTokenNotRetrievedException;
import com.internbook.internbook_backend.exceptions.DauthUserNotRetrievedException;
import com.internbook.internbook_backend.mappers.UserMapper;
import com.internbook.internbook_backend.repositories.DepartmentRepository;
import com.internbook.internbook_backend.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    @Value("${spring.dauth.tokenEndpoint}")
    private String dauthTokenEndpoint;

    @Value("${spring.dauth.clientId}")
    private String dauthClientId;

    @Value("${spring.dauth.clientSecret}")
    private String dauthClientSecret;

    @Value("${spring.dauth.redirectUri}")
    private String dauthRedirectUri;

    @Value("${spring.dauth.endpoint}")
    private String dauthUserEndpoint;

    public AuthService(UserRepository userRepository, DepartmentRepository departmentRepository, UserMapper userMapper, JwtService jwtService, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.jwtConfig = jwtConfig;
    }

    public ResponseEntity<JwtResponseDto> dauthLogin (String authorizationCode,
                                                      HttpServletResponse response) {
        DauthTokenResponseDto dauthTokenResponseDto = getDauthToken(authorizationCode);
        DauthUserApiResponseDto dauthUserApiResponseDto = getDauthUser(dauthTokenResponseDto.getAccessToken());
        var user = userRepository.findByEmail(dauthUserApiResponseDto.getEmail());
        UserDto userDto;
        if(user == null) {
            userDto = createUser(dauthUserApiResponseDto);
        }
        else {
            userDto = userMapper.toDto(user);
        }
        var jwtAccessToken = jwtService.generateAccessToken(userDto);
        var jwtRefreshToken = jwtService.generateRefreshToken(userDto);
        createCookieForRefreshToken(response, jwtRefreshToken);
        return ResponseEntity.ok(new JwtResponseDto(jwtAccessToken, userDto));
    }

    public ResponseEntity<RefreshTokenResponseDto> refresh (String refreshToken) {
        if(!jwtService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var userId = jwtService.getIdFromToken(refreshToken);
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken = jwtService.generateAccessToken(userMapper.toDto(user));
        return ResponseEntity.ok(new RefreshTokenResponseDto(accessToken));
    }

    private void createCookieForRefreshToken(HttpServletResponse response, String jwtRefreshToken) {
        var cookie = new Cookie("refreshToken", jwtRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    private DauthTokenResponseDto getDauthToken (String code) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", dauthClientId);
        body.add("client_secret", dauthClientSecret);
        body.add("redirect_uri", dauthRedirectUri);
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<DauthTokenResponseDto> response = restTemplate.postForEntity(
                dauthTokenEndpoint,
                request,
                DauthTokenResponseDto.class);
        if(response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new DauthTokenNotRetrievedException();
        }
        return response.getBody();
    }

    private DauthUserApiResponseDto getDauthUser (String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<DauthUserApiResponseDto> response = restTemplate.exchange(
                dauthUserEndpoint,
                HttpMethod.POST,
                request,
                DauthUserApiResponseDto.class);
        if(response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new DauthUserNotRetrievedException();
        }
        return response.getBody();
    }

    private UserDto createUser (DauthUserApiResponseDto dauthUserApiResponseDto) {
        var user = getUser(dauthUserApiResponseDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private User getUser(DauthUserApiResponseDto dauthUserApiResponseDto) {
        var user = new User();
        user.setEmail(dauthUserApiResponseDto.getEmail());
        user.setName(dauthUserApiResponseDto.getName());
        user.setId(getRollNumberFromEmail(dauthUserApiResponseDto.getEmail()));
        user.setDegree(getDegreeFromBatch(dauthUserApiResponseDto.getBatch()));
        user.setYear(getYearFromBatch(dauthUserApiResponseDto.getBatch()));
        user.setDepartment(getDepartmentByEmail(dauthUserApiResponseDto.getEmail()));
        return user;
    }

    private Long getRollNumberFromEmail (String email) {
        String rollNumber = email.split("@")[0];
        return  Long.parseLong(rollNumber);
    }

    private Degree getDegreeFromBatch (String batch) {
        String degree = batch.split(" ")[0];
        if(degree.equals("BTech")) return Degree.BTECH;
        else if(degree.equals("MTech")) return Degree.MTECH;
        else return Degree.MCA;
    }

    private Integer getYearFromBatch (String batch) {
        String yearString = batch.split(" ")[1];
        YearConfig yearConfig = new YearConfig();
        return yearConfig.getYearConfig().get(yearString);
    }

    private Department getDepartmentByEmail (String email) {
        String rollNumber = email.split("@")[0];
        DepartmentConfig departmentConfig = new DepartmentConfig();
        String departmentName = departmentConfig.getDepartments().get(rollNumber.substring(0, 4));
        return (Department) departmentRepository.findByName(departmentName);
    }
}
