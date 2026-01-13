package com.toolborrow.backend.model.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReservationDto {
    private @NonNull Long toolId;
    private @NonNull LocalDate dateFrom;
    private @NonNull LocalDate dateTo;
    private @NonNull Long borrowerUserId;
}
