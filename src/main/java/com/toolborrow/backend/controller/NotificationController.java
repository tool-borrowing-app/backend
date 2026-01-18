package com.toolborrow.backend.controller;

import com.toolborrow.backend.model.dto.NotificationDto;
import com.toolborrow.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllLoggedInUsersNotification(
            @RequestParam(name = "acknowledged", required = false) Boolean acknowledged
    ) {
        List<NotificationDto> notificationDtoList = notificationService.getAllLoggedInUsersNotification(acknowledged);
        return new ResponseEntity<>(notificationDtoList, HttpStatus.OK);
    }

    @PostMapping("/acknowledge/{id}")
    public ResponseEntity<NotificationDto> acknowledge(
            @PathVariable(name = "id") Long id
    ) {
        NotificationDto notificationDto = notificationService.acknowledgeNotification(id);
        return new ResponseEntity<>(notificationDto, HttpStatus.OK);
    }

}
