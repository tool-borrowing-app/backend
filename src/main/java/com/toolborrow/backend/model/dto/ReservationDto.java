package com.toolborrow.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Long id;
    private ToolDto toolDto;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private LookupDto status;
    private Long ownerScore;
    private String ownerComment;
    private Long borrowerScore;
    private String borrowerComment;
    private UserProfileDto borrower;
}
