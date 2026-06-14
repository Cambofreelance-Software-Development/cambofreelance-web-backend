package com.cambofreelance.authenticationservice.logger.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final Environment environment;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .defaultHeader("SERVICE-NAME", environment.getProperty("spring.application.name"))
            .build();
    }
}
