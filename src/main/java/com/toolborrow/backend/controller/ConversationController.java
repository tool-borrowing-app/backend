package com.toolborrow.backend.controller;

import com.toolborrow.backend.model.dto.ConversationDto;
import com.toolborrow.backend.model.dto.StartConversationDto;
import com.toolborrow.backend.service.ConversationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping
    public ResponseEntity<List<ConversationDto>> getAllMyConversations() {
        List<ConversationDto> conversationDtoList = conversationService.getMyConversations();
        return new ResponseEntity<>(conversationDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> getConversationById(final @PathVariable @NonNull Long id) {
        ConversationDto conversationDto = conversationService.getById(id);
        return new ResponseEntity<>(conversationDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ConversationDto> startConversation(final @RequestBody @NonNull StartConversationDto startConversationDto) {
        ConversationDto conversationDto = conversationService.createConversation(startConversationDto);
        return new ResponseEntity<>(conversationDto, HttpStatus.CREATED);
    }

}
