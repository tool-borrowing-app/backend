package com.toolborrow.backend.model.dto;

import com.toolborrow.backend.model.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long id;
    private Date createdAt;
    private String message;
    private String reference;
    private Boolean acknowledged;
    private NotificationType type;

}
