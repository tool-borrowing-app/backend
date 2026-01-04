package com.toolborrow.backend.model.dto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class CreateCheckoutSessionDto {
    private @NonNull Long toolId;
    private @NonNull LocalDate dateFrom;
    private @NonNull LocalDate dateTo;
    private @NonNull Long borrowerUserId;
}