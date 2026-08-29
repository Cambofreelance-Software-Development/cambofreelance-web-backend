package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.NotificationService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/cms/notifications")
    @PreAuthorize("hasAuthority('notifications.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Boolean isRead,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = notificationService.list(type, isRead, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/notifications/unread-count")
    @PreAuthorize("hasAuthority('notifications.view')")
    public ResponseEntity<Object> unreadCount() {
        var result = Map.of("unread", notificationService.unreadCount());
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PatchMapping("/cms/notifications/{id}/read")
    @PreAuthorize("hasAuthority('notifications.update')")
    public ResponseEntity<Object> markRead(@PathVariable String id,
                                           @RequestParam(defaultValue = "true") boolean read) {
        var result = notificationService.markRead(id, read);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/notifications/{id}")
    @PreAuthorize("hasAuthority('notifications.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        var result = notificationService.delete(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
