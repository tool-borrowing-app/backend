package com.toolborrow.backend.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class ToolDto {
    private Long id;
    private @NonNull String name;
    private @NonNull String description;
    private @NonNull Long rentalPrice;
    private @NonNull Long depositPrice;
    private @NonNull LookupDto lookupStatus;
    private UserProfileDto user;
}
