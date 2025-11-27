package com.toolborrow.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        Map<String, String> env = System.getenv();
        System.out.println("=== ENV DEBUG START ===");
        System.out.println("DB_HOST=" + env.get("DB_HOST"));
        System.out.println("DB_PORT=" + env.get("DB_PORT"));
        System.out.println("DB_NAME=" + env.get("DB_NAME"));
        System.out.println("DB_USER=" + env.get("DB_USER"));
        System.out.println("DB_PASSWORD=" + env.get("DB_PASSWORD"));
        System.out.println("=== ENV DEBUG END ===");

        SpringApplication.run(BackendApplication.class, args);
    }
}
