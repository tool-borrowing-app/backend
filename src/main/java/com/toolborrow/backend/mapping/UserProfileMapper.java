package com.toolborrow.backend.mapping;

import com.toolborrow.backend.model.dto.UserFullProfileDto;
import com.toolborrow.backend.model.dto.UserProfileDto;
import com.toolborrow.backend.model.entity.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMapper {

    public @NonNull UserProfileDto convert(User user) {
        if (user == null) return null;

        return new UserProfileDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public @NonNull UserFullProfileDto toFullDto(final @NonNull User entity) {
        return UserFullProfileDto.builder()
            .id(entity.getId())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .email(entity.getEmail())
            .phoneNumber(entity.getPhoneNumber())
            .postalCode(entity.getPostalCode())
            .city(entity.getCity())
            .streetAddress(entity.getStreetAddress())
            .build();
    }
}
