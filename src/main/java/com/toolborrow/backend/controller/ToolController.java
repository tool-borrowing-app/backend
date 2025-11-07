package com.toolborrow.backend.controller;

import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.service.ToolService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
@RequiredArgsConstructor
@Validated
public class ToolController {

    private final @NonNull ToolService service;

    @GetMapping
    public @NonNull ResponseEntity<List<Tool>> list() {
        final @NonNull List<Tool> items = service.list();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public @NonNull ResponseEntity<Tool> get(
        @PathVariable("id") final @NonNull Long id
    ) {
        return service.get(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public @NonNull ResponseEntity<Tool> create(
        @RequestBody final @NonNull Tool tool
    ) {
        final @NonNull Tool saved = service.create(tool);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public @NonNull ResponseEntity<Tool> update(
        @PathVariable("id") final @NonNull Long id,
        @RequestBody final @NonNull Tool tool
    ) {
        final @NonNull Tool updated = service.update(id, tool);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public @NonNull ResponseEntity<Void> delete(
        @PathVariable("id") final @NonNull Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
