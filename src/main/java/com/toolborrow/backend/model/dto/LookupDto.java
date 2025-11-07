package com.toolborrow.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LookupDto {
    private @NonNull String code;
    private @NonNull String name;
}
