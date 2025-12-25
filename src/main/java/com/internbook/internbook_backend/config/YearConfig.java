package com.internbook.internbook_backend.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Getter
public class YearConfig {
    private final Map<String, Integer> yearConfig = Map.of(
            "First", 1,
            "Second", 2,
            "Third", 3,
            "Fourth", 4,
            "Fifth", 5
    );
}
