package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.PartnerApplicationRequest;
import com.cambofreelance.webbackend.dto.request.PartnerPayoutRequest;
import com.cambofreelance.webbackend.dto.request.PartnerReviewRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.PartnerService;
import jakarta.validation.Valid;
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
public class PartnerController {

    private final PartnerService partnerService;

    // ── Applicant ───────────────────────────────────────────────────────────

    @GetMapping("/partner/application")
    public ResponseEntity<Object> myApplication(
        @RequestHeader(value = Constants.USER_ID) String userId
    ) {
        var result = partnerService.getMyApplication(userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/partner/application")
    public ResponseEntity<Object> submitApplication(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @Valid @RequestBody PartnerApplicationRequest request
    ) {
        var result = partnerService.submitApplication(userId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/partner/application")
    public ResponseEntity<Object> updateApplication(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @Valid @RequestBody PartnerApplicationRequest request
    ) {
        var result = partnerService.updateApplication(userId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/partner/application/withdraw")
    public ResponseEntity<Object> withdrawApplication(
        @RequestHeader(value = Constants.USER_ID) String userId
    ) {
        var result = partnerService.withdrawApplication(userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/partner/portal")
    public ResponseEntity<Object> myPortal(
        @RequestHeader(value = Constants.USER_ID) String userId
    ) {
        var result = partnerService.getMyPortal(userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** "View details" on a referred-clients row — {@code userId} in the path must be one of the caller's referrals. */
    @GetMapping("/partner/portal/clients/{userId}")
    public ResponseEntity<Object> referredClientDetail(
        @RequestHeader(value = Constants.USER_ID) String partnerId,
        @PathVariable("userId") String userId
    ) {
        var result = partnerService.getReferredClientDetail(partnerId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin ───────────────────────────────────────────────────────────────

    @GetMapping("/cms/partners")
    @PreAuthorize("hasAuthority('partner.view')")
    public ResponseEntity<Object> adminList(
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = partnerService.adminList(status, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** Admin "Approved Partners" tab — pass applicationId to drill into one partner's clients,
     *  omit it for the flat directory across every APPROVED partner. */
    @GetMapping("/cms/partners/clients")
    @PreAuthorize("hasAuthority('partner.view')")
    public ResponseEntity<Object> adminListAllClients(
        @RequestParam(required = false) String applicationId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String subStatus,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = partnerService.adminListAllReferredClients(applicationId, search, subStatus, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/partners/clients/{userId}")
    @PreAuthorize("hasAuthority('partner.view')")
    public ResponseEntity<Object> adminClientDetail(@PathVariable String userId) {
        var result = partnerService.adminGetReferredClientDetail(userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/partners/{id}")
    @PreAuthorize("hasAuthority('partner.view')")
    public ResponseEntity<Object> adminGet(@PathVariable String id) {
        var result = partnerService.adminGet(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/cms/partners/{id}/review")
    @PreAuthorize("hasAuthority('partner.review')")
    public ResponseEntity<Object> adminReview(
        @PathVariable String id,
        @Valid @RequestBody PartnerReviewRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String adminId
    ) {
        var result = partnerService.adminReview(id, request, adminId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/partners/{id}/payouts")
    @PreAuthorize("hasAuthority('partner.view')")
    public ResponseEntity<Object> listPayouts(@PathVariable String id) {
        var result = partnerService.listPayouts(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/partners/{id}/payouts")
    @PreAuthorize("hasAuthority('partner.payout')")
    public ResponseEntity<Object> recordPayout(
        @PathVariable String id,
        @Valid @RequestBody PartnerPayoutRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String adminId
    ) {
        var result = partnerService.recordPayout(id, request, adminId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }
}
