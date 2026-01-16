package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.ReviewDataDto;
import com.toolborrow.backend.model.dto.ReviewStatisticsDto;
import com.toolborrow.backend.model.entity.Reservation;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.repository.ReservationRepository;
import com.toolborrow.backend.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final @NonNull UserRepository userRepository;
    private final @NonNull ReservationRepository reservationRepository;

    @Override
    public @NonNull List<Tool> getToolsForUser(Long id) {
        var user = userRepository.findById(id).get();
        var tools = user.getTools();

        for (Tool tool : tools) {
            tool.setUser(null);
        }

        return tools;
    }

    @Override
    public @NonNull ReviewStatisticsDto getReviewStatistics(final @NonNull Long id) {
        final List<Reservation> ownerReservations = reservationRepository.findByTool_User_Id(id);

        final List<ReviewDataDto> asOwner = ownerReservations.stream()
            .filter(r -> r.getOwnerScore() != null)
            .map(r -> ReviewDataDto.builder()
                .score(r.getOwnerScore())
                .comment(r.getOwnerComment())
                .build())
            .toList();

        final List<Reservation> borrowerReservations = reservationRepository.findByUserIdBorrow_Id(id);

        final List<ReviewDataDto> asBorrower = borrowerReservations.stream()
            .filter(r -> r.getBorrowerScore() != null)
            .map(r -> ReviewDataDto.builder()
                .score(r.getBorrowerScore())
                .comment(r.getBorrowerComment())
                .build())
            .toList();

        final double average = Stream.concat(asOwner.stream(), asBorrower.stream())
            .mapToDouble(d -> d.getScore().doubleValue())
            .average()
            .orElse(0.0);

        return ReviewStatisticsDto.builder()
            .asOwner(asOwner)
            .asBorrower(asBorrower)
            .averageRating(average)
            .build();
    }
}
