package com.toolborrow.backend.service;

import com.google.api.gax.rpc.UnauthenticatedException;
import com.toolborrow.backend.mapping.MessageMapper;
import com.toolborrow.backend.model.dto.MessageDto;
import com.toolborrow.backend.model.dto.SendMessageDto;
import com.toolborrow.backend.model.entity.Conversation;
import com.toolborrow.backend.model.entity.Message;
import com.toolborrow.backend.repository.ConversationRepository;
import com.toolborrow.backend.repository.MessageRepository;
import com.toolborrow.backend.repository.UserRepository;
import com.toolborrow.backend.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public MessageDto createMessage(SendMessageDto payload) {
        Conversation conversation = conversationRepository.findById(payload.getConversationId()).orElseThrow(
                () -> new EntityNotFoundException("Conversation with id " + payload.getConversationId() + " was not found.")
        );
        String loggedInUserMail = JwtUtils.getCurrentUserEmail();
        if (!conversation.getRenter().getEmail().equals(loggedInUserMail) && !conversation.getTool().getUser().getEmail().equals(loggedInUserMail)) {
            throw new AccessDeniedException("You don't have access to this conversation.");
        }

        Message message = new Message();
        message.setText(payload.getText());
        message.setSentBy(userRepository.findByEmail(loggedInUserMail));
        message.setConversation(conversation);
        message.setSeenByReceiver(false);

        Message savedMessage = messageRepository.save(message);
        return messageMapper.toDto(savedMessage);
    }

    @Override
    public List<MessageDto> getMessages(Long conversationId) {

        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(
                () -> new EntityNotFoundException("Conversation with id " + conversationId + " was not found.")
        );

        String loggedInUserMail = JwtUtils.getCurrentUserEmail();
        if (!conversation.getRenter().getEmail().equals(loggedInUserMail) && !conversation.getTool().getUser().getEmail().equals(loggedInUserMail)) {
            throw new AccessDeniedException("You don't have access to this conversation.");
        }

        List<Message> messages = conversation.getMessages();

        return messages.stream().map(message -> messageMapper.toDto(message)).collect(Collectors.toList());
    }

}
