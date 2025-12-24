package com.toolborrow.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversationDto {

    private Long id;

    private ToolDto toolDto;

    private UserProfileDto renter;

    private UserProfileDto lender;

}
