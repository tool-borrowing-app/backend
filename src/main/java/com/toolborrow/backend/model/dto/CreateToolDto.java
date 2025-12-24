package com.toolborrow.backend.model.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;

@Data
public class CreateToolDto {
    private @NonNull String name;
    private @NonNull String description;
    private @NonNull Long rentalPrice;
    private @NonNull Long depositPrice;
    private @NonNull LookupDto lookupStatus;
    private @NonNull LookupDto lookupCategory;
    private @NonNull ArrayList<String> images;  // note: here we are expecting to recieve an "array" of base64 image (base64 images are strings ofc)
}
