package com.toolborrow.backend.model.dto;

public record UserProfileDto(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
