package com.toolborrow.backend.repository;

import com.toolborrow.backend.model.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool, Long> {
}
