package com.toolborrow.backend.controller;

import com.toolborrow.backend.model.dto.MessageDto;
import com.toolborrow.backend.model.dto.SendMessageDto;
import com.toolborrow.backend.model.entity.Conversation;
import com.toolborrow.backend.service.MessageService;
import com.toolborrow.backend.utils.JwtUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(final @RequestBody @NonNull SendMessageDto payload) {
        MessageDto messageDto = messageService.createMessage(payload);
        return new ResponseEntity<>(messageDto, HttpStatus.CREATED);
    }

}
