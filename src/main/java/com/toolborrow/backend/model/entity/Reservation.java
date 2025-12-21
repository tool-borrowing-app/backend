package com.toolborrow.backend.model.entity;

import com.toolborrow.backend.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

    @Column(name = "date_time_from", nullable = false)
    private LocalDateTime dateTimeFrom;

    @Column(name = "date_time_to", nullable = false)
    private LocalDateTime dateTimeTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lookup_id_status", nullable = false)
    private Lookup status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id_borrow", nullable = false)
    private User userIdBorrow;

    @Column(name = "rental_score")
    private Long rentalScore;

    @Column(name = "rental_comment")
    private String rentalComment;

    @Column(name = "borrower_score")
    private Long borrowerScore;

    @Column(name = "borrower_comment")
    private String borrowerComment;
}
