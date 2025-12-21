package com.toolborrow.backend.service;

import com.toolborrow.backend.exception.TBAException;
import com.toolborrow.backend.mapping.ReservationMapper;
import com.toolborrow.backend.model.dto.ReservationDto;
import com.toolborrow.backend.model.entity.Lookup;
import com.toolborrow.backend.model.entity.Reservation;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.model.entity.User;
import com.toolborrow.backend.model.enums.LookupTypeCode;
import com.toolborrow.backend.repository.LookupRepository;
import com.toolborrow.backend.repository.ReservationRepository;
import com.toolborrow.backend.repository.ToolRepository;
import com.toolborrow.backend.repository.UserRepository;
import com.toolborrow.backend.utils.JwtUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ToolRepository toolRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;
    private final LookupRepository lookupRepository;

    public ReservationDto createReservation(final @NonNull ReservationDto reservation) {
        final @NonNull Lookup status = resolveToolStatus("ACTIVE");
        final String email = JwtUtils.getCurrentUserEmail();
        final User user = userRepository.findByEmail(email);
        final Tool tool = toolRepository.findById(reservation.getToolDto().getId()).orElseThrow(() -> new TBAException(
            NOT_FOUND, "Tool not found with id " + reservation.getToolDto().getId()));

        final @NonNull Reservation entity = reservationMapper.from(reservation, user, tool, status);
        return reservationMapper.from(reservationRepository.save(entity));
    }

    // ===============================================================================

    private @NonNull Lookup resolveToolStatus(final @NonNull String statusCode) {
        final @NonNull String lookupTypeCode = LookupTypeCode.RESERVATION_STATUS.getCode();

        return lookupRepository
            .findByCodeAndLookupTypeCode(statusCode, lookupTypeCode)
            .orElseThrow(() -> new TBAException(
                NOT_FOUND,
                "Reservation status lookup not found: code=%s type=%s".formatted(statusCode, lookupTypeCode)
            ));
    }
}
