package com.toolborrow.backend.service;

import com.toolborrow.backend.exception.TBAException;
import com.toolborrow.backend.model.entity.Lookup;
import com.toolborrow.backend.model.entity.LookupType;
import com.toolborrow.backend.model.entity.Reservation;
import com.toolborrow.backend.model.enums.LookupTypeCode;
import com.toolborrow.backend.repository.LookupRepository;
import com.toolborrow.backend.repository.ReservationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledService {

    private final ReservationRepository reservationRepository;
    private final LookupRepository lookupRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void closeReservations() {
        final @NonNull LocalDate today = LocalDate.now();

        log.info("ScheduledService.closeReservations started for date {}", today);

        final @NonNull Lookup finishedStatus = resolveReservationStatus("FINISHED");

        final @NonNull List<Reservation> reservationsToClose = reservationRepository.findReservationsToClose(today);

        if (reservationsToClose.isEmpty()) {
            log.info("No reservations to close for date {}", today);
            return;
        }

        for (final Reservation r : reservationsToClose) {
            if (r.getStatus() != null && "ACTIVE".equals(r.getStatus().getCode())) {
                r.setStatus(finishedStatus);
            }
        }

        reservationRepository.saveAll(reservationsToClose);
        log.info("Closed {} reservations (set to FINISHED).", reservationsToClose.size());
    }

    // ===============================================================================

    private @NonNull Lookup resolveReservationStatus(final @NonNull String statusCode) {
        final @NonNull String lookupTypeCode = LookupTypeCode.RESERVATION_STATUS.getCode();

        return lookupRepository
            .findByCodeAndLookupTypeCode(statusCode, lookupTypeCode)
            .orElseThrow(() -> new TBAException(
                NOT_FOUND,
                "Reservation status lookup not found: code=%s type=%s".formatted(statusCode, lookupTypeCode)
            ));
    }
}
