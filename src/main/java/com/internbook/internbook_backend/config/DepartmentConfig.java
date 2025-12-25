package com.internbook.internbook_backend.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Configuration
public class DepartmentConfig {
    private final Map<String, String> departments = Map.of(
            "1061", "CSE",
            "1081", "ECE",
            "1071", "EEE",
            "1101", "ICE",
            "1111", "MECH",
            "1021", "CHEM",
            "1031", "CIVIL",
            "1121", "MME",
            "1141", "PROD"
    );
}
