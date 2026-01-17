package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    List<NotificationDto> getAllLoggedInUsersNotification(Boolean acknowledged);

}
