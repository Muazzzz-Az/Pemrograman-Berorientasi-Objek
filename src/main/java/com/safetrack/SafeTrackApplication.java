package com.safetrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // aktifkan @CreatedDate di entity
public class SafeTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafeTrackApplication.class, args);
    }
}
