package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.UserRegisterDto;
import com.toolborrow.backend.model.entity.User;
import com.toolborrow.backend.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final @NonNull UserRepository userRepository;

    @NonNull
    @Override
    public boolean register(@NonNull UserRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPostalCode(dto.getPostalCode());
        user.setCity(dto.getCity());
        user.setStreetAddress(dto.getStreetAddress());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        System.out.println("register success");

        return true;
    }
}
