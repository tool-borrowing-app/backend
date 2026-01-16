package com.toolborrow.backend.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewStatisticsDto {
    private List<ReviewDataDto> asOwner;
    private List<ReviewDataDto> asBorrower;
    private Double averageRating;
}
