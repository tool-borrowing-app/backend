package com.toolborrow.backend.mapping;

import com.toolborrow.backend.model.dto.LookupDto;
import com.toolborrow.backend.model.dto.ReservationDto;
import com.toolborrow.backend.model.entity.Lookup;
import com.toolborrow.backend.model.entity.Reservation;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.model.entity.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final ToolMapper toolMapper;

    public @NonNull Reservation from(
        final @NonNull ReservationDto dto,
        final @NonNull User user,
        final @NonNull Tool tool,
        final @NonNull Lookup status
    ) {
        final @NonNull Reservation entity = new Reservation();

        entity.setDateFrom(dto.getDateFrom());
        entity.setDateTo(dto.getDateTo());
        entity.setTool(tool);
        entity.setUserIdBorrow(user);
        entity.setStatus(status);

        return entity;
    }

    public @NonNull ReservationDto from(final @NonNull Reservation entity) {
        final @NonNull ReservationDto dto = new ReservationDto();

        dto.setId(entity.getId());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setToolDto(toolMapper.convert(entity.getTool()));

        if (entity.getStatus() != null) {
            dto.setStatus(new LookupDto(
                entity.getStatus().getCode(),
                entity.getStatus().getName()
            ));
        }

        dto.setBorrowerComment(entity.getBorrowerComment());
        dto.setBorrowerScore(entity.getBorrowerScore());
        dto.setOwnerComment(entity.getOwnerComment());
        dto.setOwnerScore(entity.getOwnerScore());

        return dto;
    }
}
