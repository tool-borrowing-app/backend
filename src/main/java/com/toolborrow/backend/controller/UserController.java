package com.toolborrow.backend.controller;

import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final @NonNull UserService service;

    @GetMapping("/{id}/tools")
    public @NonNull ResponseEntity<List<Tool>> getToolsForUser(final @PathVariable @NonNull Long id) {
        return ResponseEntity.ok(service.getToolsForUser(id));
    }
}
