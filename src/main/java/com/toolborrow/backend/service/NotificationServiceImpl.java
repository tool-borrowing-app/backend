package com.toolborrow.backend.service;

import com.toolborrow.backend.mapping.NotificationMapper;
import com.toolborrow.backend.model.dto.NotificationDto;
import com.toolborrow.backend.model.entity.Notification;
import com.toolborrow.backend.repository.NotificationRepository;
import com.toolborrow.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationDto> getAllLoggedInUsersNotification(Boolean acknowledged) {
        String loggedInUserMail = JwtUtils.getCurrentUserEmail();
        List<Notification> notifications;

        if (acknowledged != null) {
            notifications = notificationRepository.findByUserEmailAndAcknowledged(loggedInUserMail, acknowledged);
        } else {
            notifications = notificationRepository.findByUserEmail(loggedInUserMail);
        }

        return notifications.stream().map(notification -> notificationMapper.toDto(notification)).collect(Collectors.toList());
    }

}
