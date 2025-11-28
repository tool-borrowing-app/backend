package com.toolborrow.backend.controller;

import com.toolborrow.backend.model.dto.UserRegisterDto;
import com.toolborrow.backend.service.AuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final @NonNull AuthService authService;

    @PostMapping("/register")
    public boolean register(@RequestBody UserRegisterDto dto) {
        return authService.register(dto);
    }
}
