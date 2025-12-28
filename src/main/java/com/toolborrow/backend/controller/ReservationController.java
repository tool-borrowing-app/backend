package com.toolborrow.backend.controller;

import com.toolborrow.backend.model.dto.CreateReservationDto;
import com.toolborrow.backend.model.dto.ReservationDto;
import com.toolborrow.backend.service.ReservationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(final @PathVariable @NonNull Long id){
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(final @RequestBody @NonNull CreateReservationDto createReservationDto) {
        return ResponseEntity.ok(reservationService.createReservation(createReservationDto));
    }

    // =================================== REVIEW ===================================

    @PostMapping("/{id}/review")
    public ResponseEntity<ReservationDto> submitReview(final @PathVariable @NonNull Long id, final @RequestBody ReservationDto reservationDto) {
        return ResponseEntity.ok(reservationService.submitReview(id, reservationDto));
    }
}
