package com.toolborrow.backend.service;

import com.toolborrow.backend.model.entity.Tool;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface ToolService {

    @NonNull List<Tool> list();

    @NonNull Optional<Tool> get(final @NonNull Long id);

    @NonNull Tool create(final @NonNull Tool tool);

    @NonNull Tool update(final @NonNull Long id, final @NonNull Tool tool, final @NonNull String statusCode);

    void delete(final @NonNull Long id);

}
