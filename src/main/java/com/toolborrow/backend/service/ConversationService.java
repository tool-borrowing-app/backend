package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.ConversationDto;
import com.toolborrow.backend.model.dto.StartConversationDto;
import lombok.NonNull;

import java.util.List;

public interface ConversationService {

    List<ConversationDto> getMyConversations();

    ConversationDto getById(final @NonNull Long id);

    ConversationDto createConversation(final @NonNull StartConversationDto startConversationPayload);

}
