package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.CreateToolDto;
import com.toolborrow.backend.model.dto.ReservationDto;
import com.toolborrow.backend.model.dto.ToolDto;
import lombok.NonNull;

import java.util.List;

public interface ToolService {

    @NonNull List<ToolDto> list();

    @NonNull ToolDto get(final @NonNull Long id);

    @NonNull ToolDto create(final @NonNull CreateToolDto tool);

    @NonNull ToolDto update(final @NonNull Long id, final @NonNull ToolDto tool);

    @NonNull void delete(final @NonNull Long id);

    @NonNull List<ReservationDto> getToolReservations(final @NonNull Long id);

}
