package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.NotificationDto;
import com.toolborrow.backend.model.entity.Notification;

import java.util.List;

public interface NotificationService {

    List<NotificationDto> getAllLoggedInUsersNotification(Boolean acknowledged);

    NotificationDto acknowledgeNotification(Long id);

    Notification createMessageNotification(Long conversationId, String receiver, String sender);

}
