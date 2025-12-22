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
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ToolRepository toolRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;
    private final LookupRepository lookupRepository;

    @Override
    public @NonNull List<ReservationDto> getReservations() {
        final @NonNull User user = userRepository.findByEmail(JwtUtils.getCurrentUserEmail());
        return reservationRepository.findByUserIdBorrow(user).stream().map(reservationMapper::from).toList();
    }

    public @NonNull ReservationDto getById(final @NonNull Long id) {
        final @NonNull User user = userRepository.findByEmail(JwtUtils.getCurrentUserEmail());
        final @NonNull Reservation entity = reservationRepository.findById(id)
            .orElseThrow(() -> new TBAException(NOT_FOUND, "Reservation not found with id: " + id));

        if (!entity.getUserIdBorrow().equals(user)) {
            throw new TBAException(BAD_REQUEST, "Reservation don't belong to the same user! Reservation id: " + id);
        }

        return reservationMapper.from(entity);
    }

    @Transactional
    public @NonNull ReservationDto createReservation(final @NonNull ReservationDto reservation) {
        final @NonNull Lookup status = resolveReservationStatus("ACTIVE");
        final @NonNull User user = userRepository.findByEmail(JwtUtils.getCurrentUserEmail());

        final @NonNull Tool tool = toolRepository.findById(reservation.getToolDto().getId())
            .orElseThrow(() -> new TBAException(
                NOT_FOUND, "Tool not found with id " + reservation.getToolDto().getId()));

        if(user.equals(tool.getUser())) {
            throw new TBAException(BAD_REQUEST, "User cannot reserve their own tool");
        }

        final @NonNull LocalDate from = reservation.getDateFrom();
        final @NonNull LocalDate to = reservation.getDateTo();
        final @NonNull LocalDate today = LocalDate.now();

        if (from.isBefore(today)) {
            throw new TBAException(BAD_REQUEST, "Reservation date cannot be in the past");
        }

        if (from.isAfter(to)) {
            throw new TBAException(BAD_REQUEST, "Invalid date range: dateFrom must be before or equal to dateTo");
        }

        final boolean overlapExists =
            reservationRepository.existsOverlappingActiveReservation(tool.getId(), from, to);

        if (overlapExists) {
            throw new TBAException(BAD_REQUEST, "Tool already reserved in the requested date range");
        }

        final @NonNull Reservation entity = reservationMapper.from(reservation, user, tool, status);
        final @NonNull Reservation saved = reservationRepository.save(entity);

        return reservationMapper.from(saved);
    }

    @Override
    @Transactional
    public @NonNull ReservationDto submitReview(final @NonNull Long id, final @NonNull ReservationDto dto) {

        final @NonNull Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new TBAException(NOT_FOUND, "Reservation not found with id: " + id));

        if (!reservation.getStatus().getCode().equals("FINISHED")) {
            throw new TBAException(BAD_REQUEST, "Reservation has not finished yet!");
        }

        final @NonNull User currentUser = userRepository.findByEmail(JwtUtils.getCurrentUserEmail());

        if (currentUser.equals(reservation.getUserIdBorrow())) {
            // kölcsönző értékel
            if (reservation.getOwnerScore() != null) {
                throw new TBAException(BAD_REQUEST, "You have already submitted a review as borrower!");
            }

            final String ownerComment = dto.getOwnerComment();
            final Long ownerScore = dto.getOwnerScore();
            validateCommentAndScore(ownerComment, ownerScore);

            reservation.setOwnerComment(ownerComment);
            reservation.setOwnerScore(ownerScore);
        } else if (currentUser.equals(reservation.getTool().getUser())) {
            // bérbeadó értékel
            if (reservation.getBorrowerScore() != null) {
                throw new TBAException(BAD_REQUEST, "You have already submitted a review as owner!");
            }

            final String borrowerComment = dto.getBorrowerComment();
            final Long borrowerScore = dto.getBorrowerScore();
            validateCommentAndScore(borrowerComment, borrowerScore);

            reservation.setBorrowerComment(borrowerComment);
            reservation.setBorrowerScore(borrowerScore);
        } else {
            throw new TBAException(BAD_REQUEST, "User not allowed to submit review to reservation with id: " + id);
        }

        final @NonNull Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.from(saved);
    }

    // ===============================================================================

    private void validateCommentAndScore(final @Nullable String comment, final @Nullable Long score) {
        if (comment == null || comment.isBlank()) {
            throw new TBAException(BAD_REQUEST, "Comment must not be empty!");
        }
        if (score == null || score < 1 || score > 5) {
            throw new TBAException(BAD_REQUEST, "Score must be between 1 and 5!");
        }
    }

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
