package com.toolborrow.backend.mapping;

import com.toolborrow.backend.model.dto.MessageDto;
import com.toolborrow.backend.model.entity.Message;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final UserProfileMapper userProfileMapper;

    public @NonNull MessageDto toDto(Message message) {
        if (message == null) return null;

        return new MessageDto(
                message.getSentAt(),
                userProfileMapper.convert(message.getSentBy()),
                message.getText(),
                message.getSeenByReceiver()
        );
    }

}
