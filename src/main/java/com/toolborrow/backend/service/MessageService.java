package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.MessageDto;
import com.toolborrow.backend.model.dto.SendMessageDto;

import java.util.List;

public interface MessageService {

    MessageDto createMessage(SendMessageDto payload);

    List<MessageDto> getMessages(Long conversationId);

}
