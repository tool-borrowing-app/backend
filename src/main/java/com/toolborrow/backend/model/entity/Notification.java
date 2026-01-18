package com.toolborrow.backend.model.entity;

import com.toolborrow.backend.model.entity.base.BaseEntity;
import com.toolborrow.backend.model.entity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "message")
    private String message;

    @Column(name = "reference")
    private String reference;

    @Column(name = "acknowledged")
    private Boolean acknowledged;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    private User user;
}
