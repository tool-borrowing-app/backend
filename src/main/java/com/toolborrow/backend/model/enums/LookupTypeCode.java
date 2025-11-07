package com.toolborrow.backend.model.enums;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum LookupTypeCode {
    TOOL_STATUS("TOOL_STATUS"),
    TOOL_CATEGORY("TOOL_CATEGORY"),
    RESERVATION_STATUS("RESERVATION_STATUS");

    private @NonNull String code;

    LookupTypeCode(final @NonNull String code) {
        this.code = code;
    }
}
