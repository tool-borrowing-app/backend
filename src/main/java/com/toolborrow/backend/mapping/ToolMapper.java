package com.toolborrow.backend.mapping;

import com.toolborrow.backend.model.dto.ToolDto;
import com.toolborrow.backend.model.entity.Tool;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ToolMapper {

    public @NonNull Tool convert(final @NonNull ToolDto toolDto) {
        final @NonNull Tool result = new Tool();

        result.setName(toolDto.getName());
        result.setDescription(toolDto.getDescription());
        result.setDepositPrice(toolDto.getDepositPrice());
        result.setRentalPrice(toolDto.getRentalPrice());

        return result;
    }

    public @NonNull ToolDto convert(final @NonNull Tool tool) {
        final @NonNull ToolDto result = new ToolDto();

        result.setName(tool.getName());
        result.setDescription(tool.getDescription());
        result.setDepositPrice(tool.getDepositPrice());
        result.setRentalPrice(tool.getRentalPrice());

        return result;
    }
}
