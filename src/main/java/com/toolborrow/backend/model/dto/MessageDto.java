package com.toolborrow.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MessageDto {

    private Date sentAt;
    private UserProfileDto sentBy;
    private String text;
    private Boolean seenByReceiver;

}
