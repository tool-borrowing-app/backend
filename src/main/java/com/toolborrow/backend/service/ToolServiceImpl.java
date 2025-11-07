package com.toolborrow.backend.service;

import com.toolborrow.backend.mapping.ToolMapper;
import com.toolborrow.backend.model.dto.ToolDto;
import com.toolborrow.backend.model.entity.Lookup;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.model.enums.LookupTypeCode;
import com.toolborrow.backend.repository.LookupRepository;
import com.toolborrow.backend.repository.ToolRepository;
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
    private final @NonNull ToolMapper toolMapper;

    @Override
    public @NonNull List<ToolDto> list() {
        final @NonNull List<Tool> all = toolRepository.findAll();
        return all
            .stream()
            .map(toolMapper::convert)
            .toList();
    }

    @Override
    public @NonNull Optional<ToolDto> get(final @NonNull Long id) {
        return toolRepository
            .findById(id)
            .map(toolMapper::convert);
    }

    @Override
    public @NonNull ToolDto create(final @NonNull ToolDto tool) {
        final @NonNull Lookup status = resolveToolStatus("ACTIVE");

        final @NonNull Tool entity = toolMapper.convert(tool);
        entity.setStatus(status);

        final @NonNull Tool saved = toolRepository.save(entity);
        return toolMapper.convert(saved);
    }

    @Override
    public @NonNull ToolDto update(
        final @NonNull Long id,
        final @NonNull ToolDto tool,
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

        final @NonNull Tool saved = toolRepository.save(current);
        return toolMapper.convert(saved);
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
