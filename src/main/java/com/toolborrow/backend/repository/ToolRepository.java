package com.toolborrow.backend.repository;

import com.toolborrow.backend.model.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToolRepository extends JpaRepository<Tool, Long> {

    List<Tool> findByUserId(Long userId);
}
