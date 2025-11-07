package com.toolborrow.backend.service.impl;

import com.toolborrow.backend.model.entity.Lookup;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.model.enums.LookupTypeCode;
import com.toolborrow.backend.repository.LookupRepository;
import com.toolborrow.backend.repository.ToolRepository;
import com.toolborrow.backend.service.ToolService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ToolServiceImpl implements ToolService {

    private final @NonNull ToolRepository toolRepository;
    private final @NonNull LookupRepository lookupRepository;

    @Override
    public @NonNull List<Tool> list() {
        final @NonNull List<Tool> all = toolRepository.findAll();
        return all;
    }

    @Override
    public @NonNull Optional<Tool> get(final @NonNull Long id) {
        return toolRepository.findById(id);
    }

    @Override
    public @NonNull Tool create(final @NonNull Tool tool) {
        final @NonNull Lookup status = resolveToolStatus("ACTIVE");
        tool.setStatus(status);
        return toolRepository.save(tool);
    }

    @Override
    public @NonNull Tool update(
        final @NonNull Long id,
        final @NonNull Tool tool,
        final @NonNull String statusCode
    ) {
        final @NonNull Tool current = toolRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tool not found: " + id));

        current.setName(tool.getName());
        current.setDescription(tool.getDescription());
        current.setRentalPrice(tool.getRentalPrice());
        current.setDepositPrice(tool.getDepositPrice());

        final @NonNull Lookup status = resolveToolStatus(statusCode);
        current.setStatus(status);

        return toolRepository.save(current);
    }

    @Override
    public void delete(final @NonNull Long id) {
        if (!toolRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Tool not found: " + id);
        }
        toolRepository.deleteById(id);
    }

    // ===============================================================================

    private @NonNull Lookup resolveToolStatus(final @NonNull String statusCode) {
        final @NonNull String lookupTypeCode = LookupTypeCode.TOOL_STATUS.getCode();

        return lookupRepository
            .findByCodeAndLookupTypeCode(statusCode, lookupTypeCode)
            .orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND,
                "Tool status lookup not found: code=%s type=%s".formatted(statusCode, lookupTypeCode)
            ));
    }
}
