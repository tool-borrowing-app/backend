package com.toolborrow.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Long id;
    private ToolDto toolDto;
    private LocalDateTime dateTimeFrom;
    private LocalDateTime dateTimeTo;
    private LookupDto status;
    private Long rentalScore;
    private String rentalComment;
    private Long borrowerScore;
    private String borrowerComment;
}
