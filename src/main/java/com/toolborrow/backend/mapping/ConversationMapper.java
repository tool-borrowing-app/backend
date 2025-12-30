package com.toolborrow.backend.mapping;

import com.toolborrow.backend.model.dto.ConversationDto;
import com.toolborrow.backend.model.entity.Conversation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConversationMapper {

    private final ToolMapper toolMapper;
    private final UserProfileMapper userProfileMapper;

    public @NonNull ConversationDto toDto(Conversation conversation) {
        if (conversation == null) return null;

        return new ConversationDto(
                conversation.getId(),
                toolMapper.convert(conversation.getTool()),
                userProfileMapper.convert(conversation.getRenter()),
                userProfileMapper.convert(conversation.getTool().getUser())
        );
    }

}
