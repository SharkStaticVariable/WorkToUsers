package io.project.worktousers.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ApplicationConfiguration {
    @PostConstruct
    public void init(){
        log.warn("app is loaded!");
    }
}
