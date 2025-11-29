package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.UserLoginDto;
import com.toolborrow.backend.model.dto.UserRegisterDto;
import lombok.NonNull;

public interface AuthService {
    @NonNull
    boolean register(@NonNull UserRegisterDto dto);

    String login(@NonNull UserLoginDto dto);
}
