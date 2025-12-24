package com.toolborrow.backend.model.entity;

import com.toolborrow.backend.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "message")
public class Message extends BaseEntity {

    @Column(name = "sent_at")
    @CreationTimestamp
    private Date sentAt;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sent_by_id")
    private User sentBy;

    @Column(name = "text")
    private String text;

    @Column(name = "seen_by_receiver")
    private Boolean seenByReceiver;

}
