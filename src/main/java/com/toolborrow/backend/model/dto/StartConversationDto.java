package com.toolborrow.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class StartConversationDto {

    private @NonNull Long toolId;

}
