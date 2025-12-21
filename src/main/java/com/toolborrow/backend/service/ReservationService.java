package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.ReservationDto;
import lombok.NonNull;

public interface ReservationService {

    ReservationDto createReservation(final @NonNull ReservationDto reservation);
}
