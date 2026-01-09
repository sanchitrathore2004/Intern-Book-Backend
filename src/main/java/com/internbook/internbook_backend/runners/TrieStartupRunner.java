package com.internbook.internbook_backend.runners;

import com.internbook.internbook_backend.services.TrieService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrieStartupRunner implements ApplicationRunner {
    private final TrieService trieService;

    @Override
    public void run(ApplicationArguments args) {
        trieService.buildIndexOnStartup();
    }
}
