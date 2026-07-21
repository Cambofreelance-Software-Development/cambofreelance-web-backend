package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.dto.request.ContactRequest;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    // ── Public endpoint (rate-limited per client IP) ───────────────────────────

    @PostMapping("/contact")
    public ResponseEntity<Object> submitContact(@Valid @RequestBody ContactRequest request,
                                                HttpServletRequest httpRequest) {
        contactService.submit(request, clientIp(httpRequest));
        return new ResponseEntity<>(
            new MessageResponse("Your message has been sent successfully.", ErrorCode.SUCCESS),
            HttpStatus.OK
        );
    }

    // ── Admin endpoints ────────────────────────────────────────────────────────

    @GetMapping("/cms/contact-messages")
    @PreAuthorize("hasAuthority('contact-messages.view')")
    public ResponseEntity<Object> adminList(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean isRead,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = contactService.list(search, isRead, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/contact-messages/unread-count")
    @PreAuthorize("hasAuthority('contact-messages.view')")
    public ResponseEntity<Object> unreadCount() {
        var result = Map.of("unread", contactService.unreadCount());
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PatchMapping("/cms/contact-messages/{id}/read")
    @PreAuthorize("hasAuthority('contact-messages.update')")
    public ResponseEntity<Object> markRead(@PathVariable String id,
                                           @RequestParam(defaultValue = "true") boolean read) {
        var result = contactService.markRead(id, read);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/contact-messages/{id}")
    @PreAuthorize("hasAuthority('contact-messages.delete')")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        var result = contactService.delete(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwarded)) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
