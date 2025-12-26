package com.toolborrow.backend.model.entity;

import com.toolborrow.backend.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "conversation")
public class Conversation extends BaseEntity {

    @OneToMany(mappedBy = "conversation")
    @OrderBy("sentAt ASC")
    private List<Message> messages;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    private User renter;

}
