package com.toolborrow.backend.service;

import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final @NonNull UserRepository userRepository;

    @Override
    public @NonNull List<Tool> getToolsForUser(Long id) {
        var user = userRepository.findById(id).get();
        var tools = user.getTools();

        for (Tool tool: tools) {
            tool.setUser(null);
        }

        return tools;
    }
}
