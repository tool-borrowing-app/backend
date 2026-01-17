package com.toolborrow.backend.service;

import com.toolborrow.backend.mapping.NotificationMapper;
import com.toolborrow.backend.model.dto.NotificationDto;
import com.toolborrow.backend.model.entity.Notification;
import com.toolborrow.backend.model.entity.User;
import com.toolborrow.backend.model.entity.enums.NotificationType;
import com.toolborrow.backend.repository.NotificationRepository;
import com.toolborrow.backend.repository.UserRepository;
import com.toolborrow.backend.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

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

    @Override
    @Transactional
    public NotificationDto acknowledgeNotification(Long id) {
        String loggedInUserMail = JwtUtils.getCurrentUserEmail();
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Notification with id " + id + " was not found.")
        );

        notification.setAcknowledged(true);
        return notificationMapper.toDto(notification);
    }

    @Override
    @Transactional
    public Notification createMessageNotification(Long conversationId, String receiverEmail, String senderEmail) {
        User receiverUser = userRepository.findByEmail(receiverEmail);
        User senderUser = userRepository.findByEmail(senderEmail);

        Notification notification = new Notification();
        notification.setUser(receiverUser);
        String message = senderUser.getFirstName() + " " + senderUser.getLastName() + " üzenetet küldött neked";
        notification.setMessage(message);
        notification.setReference(conversationId.toString());
        notification.setAcknowledged(false);
        notification.setType(NotificationType.CONVERSATION);

        Notification savedNotification = notificationRepository.save(notification);
        return savedNotification;
    }

}
