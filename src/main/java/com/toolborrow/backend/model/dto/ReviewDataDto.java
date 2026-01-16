package com.toolborrow.backend.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDataDto {
    private Long score;
    private String comment;
}
