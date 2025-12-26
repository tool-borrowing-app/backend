package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.MessageDto;
import com.toolborrow.backend.model.dto.SendMessageDto;

public interface MessageService {

    MessageDto createMessage(SendMessageDto payload);

}
