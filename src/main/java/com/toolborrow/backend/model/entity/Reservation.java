package com.toolborrow.backend.model.entity;

import com.toolborrow.backend.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

    @Column(name = "date_from", nullable = false)
    private LocalDate dateFrom;

    @Column(name = "date_to", nullable = false)
    private LocalDate dateTo;

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
