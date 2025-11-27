package com.toolborrow.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class BackendApplication {

    @Autowired
    Environment env;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @PostConstruct
    public void logEnv() {
        System.out.println("=== ENV DEBUG START ===");
        System.out.println("DB_HOST=" + System.getenv("DB_HOST"));
        System.out.println("DB_PORT=" + System.getenv("DB_PORT"));
        System.out.println("DB_NAME=" + System.getenv("DB_NAME"));
        System.out.println("DB_USER=" + System.getenv("DB_USER"));
        System.out.println("DB_PASSWORD=" + System.getenv("DB_PASSWORD"));   // debug only
        System.out.println("=== ENV DEBUG END ===");
    }
}
