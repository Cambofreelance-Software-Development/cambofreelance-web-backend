package com.cambofreelance.authenticationservice;

import com.cambofreelance.authenticationservice.startup.Startup;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(
    scanBasePackages = "com.cambofreelance.authenticationservice",
    exclude = UserDetailsServiceAutoConfiguration.class
)
@Slf4j
public class AuthenticationServiceApplication {
    private final Startup startup;

    public AuthenticationServiceApplication(Startup startup) {
        this.startup = startup;
    }

    public static void main(String[] args) {
    SpringApplication.run(AuthenticationServiceApplication.class, args);
  }

    @PostConstruct
    private void init() {
        log.info("Initializing api route Manager ...");
        startup.initApiMigrate();
        log.info("Finished api route Manager ...");
    }

}
