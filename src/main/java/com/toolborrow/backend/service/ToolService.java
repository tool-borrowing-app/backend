package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.ToolDto;
import com.toolborrow.backend.model.entity.Tool;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface ToolService {

    @NonNull List<ToolDto> list();

    @NonNull Optional<ToolDto> get(final @NonNull Long id);

    @NonNull ToolDto create(final @NonNull ToolDto tool);

    @NonNull ToolDto update(final @NonNull Long id, final @NonNull ToolDto tool, final @NonNull String statusCode);

    void delete(final @NonNull Long id);

}
