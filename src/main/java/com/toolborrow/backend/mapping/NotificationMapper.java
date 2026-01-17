package com.toolborrow.backend.mapping;

import com.toolborrow.backend.model.dto.NotificationDto;
import com.toolborrow.backend.model.entity.Notification;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public @NonNull NotificationDto toDto(Notification notification) {
        if (notification == null) return null;

        return new NotificationDto(
            notification.getCreatedAt(),
            notification.getMessage(),
            notification.getReference(),
            notification.getAcknowledged(),
            notification.getType()
        );
    }

}
