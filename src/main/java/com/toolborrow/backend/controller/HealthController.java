package com.toolborrow.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
@Validated
public class HealthController {

    @GetMapping("")
    public Map<String, String> getHealth() {
        return Map.of("status", "healthy");
    }
}
