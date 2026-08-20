package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.CompanyInfoRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.ClientService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // ── Client self-service (registration workflow) ─────────────────────────

    @GetMapping("/client/me")
    public ResponseEntity<Object> myClient(@RequestHeader(Constants.USER_ID) String userId) {
        return new ResponseEntity<>(new MessageResponse(clientService.getMyClient(userId), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/client/company")
    public ResponseEntity<Object> updateCompany(
        @RequestHeader(Constants.USER_ID) String userId,
        @Valid @RequestBody CompanyInfoRequest request
    ) {
        return new ResponseEntity<>(new MessageResponse(clientService.updateCompanyInfo(userId, request), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/client/verify-email/send")
    public ResponseEntity<Object> sendVerifyEmail(@RequestHeader(Constants.USER_ID) String userId) {
        clientService.sendVerifyEmailOtp(userId);
        return new ResponseEntity<>(new MessageResponse("Verification code sent", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/client/verify-email/confirm")
    public ResponseEntity<Object> confirmVerifyEmail(
        @RequestHeader(Constants.USER_ID) String userId,
        @RequestBody Map<String, String> body
    ) {
        var result = clientService.confirmVerifyEmail(userId, body.getOrDefault("otp", ""));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin: Client Management ────────────────────────────────────────────

    @GetMapping("/cms/clients")
    @PreAuthorize("hasAuthority('client.view')")
    public ResponseEntity<Object> listClients(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String clientStatus,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return new ResponseEntity<>(new MessageResponse(clientService.adminList(search, clientStatus, page, size), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/clients/{id}")
    @PreAuthorize("hasAuthority('client.view')")
    public ResponseEntity<Object> clientDetail(@PathVariable String id) {
        return new ResponseEntity<>(new MessageResponse(clientService.adminDetail(id), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/cms/clients/{id}/status")
    @PreAuthorize("hasAuthority('client.status')")
    public ResponseEntity<Object> updateClientStatus(
        @PathVariable String id,
        @RequestBody Map<String, String> body,
        @RequestHeader(value = Constants.USER_ID, required = false) String adminId
    ) {
        var result = clientService.adminUpdateStatus(id, body.getOrDefault("clientStatus", ""), adminId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/cms/clients/{id}/company")
    @PreAuthorize("hasAuthority('client.update')")
    public ResponseEntity<Object> updateClientCompany(
        @PathVariable String id,
        @Valid @RequestBody CompanyInfoRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String adminId
    ) {
        var result = clientService.adminUpdateCompanyInfo(id, request, adminId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
