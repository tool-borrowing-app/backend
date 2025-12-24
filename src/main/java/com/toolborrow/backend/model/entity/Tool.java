package com.toolborrow.backend.model.entity;

import com.toolborrow.backend.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tool")
public class Tool extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "rental_price", nullable = false)
    private Long rentalPrice;

    @Column(name = "deposit_price", nullable = false)
    private Long depositPrice;

    @JoinColumn(name = "lookup_id_status", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lookup status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "tool")
    private List<Conversation> conversations;

}
