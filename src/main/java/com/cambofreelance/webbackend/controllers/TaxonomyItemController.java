package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.CreateTaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.DetailTaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.UpdateTaxonomyItemRequest;
import com.cambofreelance.webbackend.logger.contants.Constants;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.TaxonomyItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/taxonomies/taxonomy-items")
@RequiredArgsConstructor
public class TaxonomyItemController {

    private final TaxonomyItemService taxonomyItemService;

    @PostMapping(value = "/create")
    @Operation(summary = "1. Create New Taxonomy Item", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy-item.create')")
    public ResponseEntity<MessageResponse> createTaxonomyItem(
            @Valid @RequestBody CreateTaxonomyItemRequest request,
            @RequestHeader(value = Constants.USER_ID, required = false) String userId)
            throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyItemService.createTaxonomyItem(request, userId),
                ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    @Operation(summary = "2. Update Taxonomy Item", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy-item.update')")
    public ResponseEntity<MessageResponse> updateTaxonomyItem(
            @Valid @RequestBody UpdateTaxonomyItemRequest request,
            @RequestHeader(value = Constants.USER_ID, required = false) String userId)
            throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyItemService.updateTaxonomyItem(request, userId),
                ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "3. List All Taxonomy Item", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy-item.view')")
    public ResponseEntity<MessageResponse> listAllTaxonomyItem(
            @Valid @RequestBody BaseRequest request,
            @RequestHeader(value = Constants.USER_ID, required = false) String userId)
            throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyItemService.listAllTaxonomyItems(request),
                ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/detail")
    @Operation(summary = "4. Detail Taxonomy Item", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy-item.view')")
    public ResponseEntity<MessageResponse> findByCodeTaxonomyItem(
            @Valid @RequestBody DetailTaxonomyItemRequest request,
            @RequestHeader(value = Constants.USER_ID, required = false) String userId)
            throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyItemService.findByCode(request, userId),
                ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    @Operation(summary = "5. Delete Taxonomy Item", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy-item.delete')")
    public ResponseEntity<MessageResponse> deleteTaxonomyItem(
            @Valid @RequestBody DetailTaxonomyItemRequest request,
            @RequestHeader(value = Constants.USER_ID, required = false) String userId)
            throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyItemService.deleteByCode(request, userId),
                ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
