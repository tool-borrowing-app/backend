package com.toolborrow.backend.mapping;

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

}
