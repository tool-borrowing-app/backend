package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.ReservationDto;
import lombok.NonNull;

import java.util.List;

public interface ReservationService {

    List<ReservationDto> getReservations();

    ReservationDto getById(final @NonNull Long id);

    ReservationDto createReservation(final @NonNull ReservationDto reservation);
}
