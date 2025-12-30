package com.toolborrow.backend.service;

import com.toolborrow.backend.mapping.ConversationMapper;
import com.toolborrow.backend.model.dto.ConversationDto;
import com.toolborrow.backend.model.dto.StartConversationDto;
import com.toolborrow.backend.model.entity.Conversation;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.model.entity.User;
import com.toolborrow.backend.repository.ConversationRepository;
import com.toolborrow.backend.repository.ToolRepository;
import com.toolborrow.backend.repository.UserRepository;
import com.toolborrow.backend.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ToolRepository toolRepository;
    private final ConversationMapper conversationMapper;

    @Override
    public List<ConversationDto> getMyConversations(Long itemId) {
        String loggedInUserMail = JwtUtils.getCurrentUserEmail();
        List<Conversation> conversations = conversationRepository.findByRenterEmailOrToolUserEmail(loggedInUserMail, loggedInUserMail);
        if (itemId != null) {
            conversations.stream().filter(conversation -> conversation.getTool().getId().equals(itemId));
        }
        return conversations.stream().map(conversation -> conversationMapper.toDto(conversation)).collect(Collectors.toList());
    }

    @Override
    public ConversationDto getById(@NonNull Long id) {
        Conversation conversation = conversationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Conversation with id " + id + " was not found.")
        );

        return conversationMapper.toDto(conversation);
    }

    @Override
    @Transactional
    public ConversationDto createConversation(@NonNull StartConversationDto startConversationPayload) {

        String loggedInUserMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long toolId = startConversationPayload.getToolId();

        if (!conversationRepository.findByToolIdAndRenterEmail(startConversationPayload.getToolId(), loggedInUserMail).isEmpty()) {
            try {
                throw new BadRequestException("Conversation already exists with user " + loggedInUserMail + " on tool with id " + toolId);
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        Tool tool = toolRepository.findById(toolId).orElseThrow(
                () -> new EntityNotFoundException("Tool with id " + startConversationPayload.getToolId() + " was not found.")
        );

        User renter = userRepository.findByEmail(loggedInUserMail);

        Conversation conversation = new Conversation();
        conversation.setTool(tool);
        conversation.setRenter(renter);

        Conversation savedConversation = conversationRepository.save(conversation);

        return conversationMapper.toDto(savedConversation);
    }

    @Override
    public void deleteConversation(Long id) {
        conversationRepository.deleteById(id);
    }
}
