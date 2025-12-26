package com.toolborrow.backend.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class SendMessageDto {

    private @NonNull Long conversationId;
    private @NonNull String text;

}
