package com.toolborrow.backend.service;

import com.toolborrow.backend.model.dto.ReviewStatisticsDto;
import com.toolborrow.backend.model.entity.Tool;

import java.util.List;

public interface UserService {

    List<Tool> getToolsForUser(Long id);

    ReviewStatisticsDto getReviewStatistics(Long id);
}
