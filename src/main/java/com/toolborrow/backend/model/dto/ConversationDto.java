package com.toolborrow.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversationDto {

    private Long id;

    private ToolDto tool;

    private UserProfileDto renter;

    private UserProfileDto lender;

}
