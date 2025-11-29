package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.UserLoginDto;
import com.toolborrow.backend.model.dto.UserProfileDto;
import com.toolborrow.backend.model.dto.UserRegisterDto;
import com.toolborrow.backend.model.entity.User;
import com.toolborrow.backend.repository.UserRepository;
import com.toolborrow.backend.utils.JwtUtils;
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

    @Autowired
    private JwtUtils jwtUtils;

    private final @NonNull UserRepository userRepository;

    @NonNull
    @Override
    public boolean register(@NonNull UserRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ezt az email cím már egy másik fiók használja!");
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

    @Override
    public String login(@NonNull UserLoginDto dto) {
        User user = userRepository.findByEmail(dto.getEmail());

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtUtils.generateToken(user.getEmail());
    }

    public UserProfileDto getProfileFromToken(String token) {
        String email = jwtUtils.validateAndExtractEmail(token);  // implement this
        var user = userRepository.findByEmail(email);

        return new UserProfileDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
