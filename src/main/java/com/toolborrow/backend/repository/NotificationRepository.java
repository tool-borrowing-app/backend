package com.toolborrow.backend.repository;

import com.toolborrow.backend.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserEmail(String userEmail);

    List<Notification> findByUserEmailAndAcknowledged(String userEmail, Boolean acknowledged);

}
