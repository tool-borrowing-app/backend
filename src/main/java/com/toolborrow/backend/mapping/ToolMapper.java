package com.toolborrow.backend.mapping;

import com.toolborrow.backend.model.dto.LookupDto;
import com.toolborrow.backend.model.dto.ToolDto;
import com.toolborrow.backend.model.entity.Lookup;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.model.entity.User;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ToolMapper {

    public @NonNull Tool convert(
        final @NonNull ToolDto toolDto,
        final @NonNull Lookup status,
        final @NonNull User user
    ) {
        final @NonNull Tool result = new Tool();

        result.setName(toolDto.getName());
        result.setDescription(toolDto.getDescription());
        result.setDepositPrice(toolDto.getDepositPrice());
        result.setRentalPrice(toolDto.getRentalPrice());
        result.setStatus(status);
        result.setUser(user);

        return result;
    }

    public @NonNull ToolDto convert(final @NonNull Tool tool) {
        final @NonNull ToolDto result = new ToolDto();

        result.setId(tool.getId());
        result.setName(tool.getName());
        result.setDescription(tool.getDescription());
        result.setDepositPrice(tool.getDepositPrice());
        result.setRentalPrice(tool.getRentalPrice());

        if (tool.getStatus() != null) {
            result.setLookupStatus(new LookupDto(
                tool.getStatus().getCode(),
                tool.getStatus().getName()
            ));
        }

        return result;
    }
}
