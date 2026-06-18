package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.CreateTaxonomyRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.DetailTaxonomyRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.UpdateTaxonomyRequest;
import com.cambofreelance.webbackend.logger.contants.Constants;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.TaxonomyService;
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
@RequestMapping("/api/taxonomies")
@RequiredArgsConstructor
public class TaxonomyController {

    private final TaxonomyService taxonomyService;

    @PostMapping(value = "/create")
    @Operation(summary = "1. Create New Taxonomy", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy.create')")
    public ResponseEntity<MessageResponse> createTaxonomy(
            @Valid @RequestBody CreateTaxonomyRequest request,
            @RequestHeader(value = Constants.USER_ID, required = false) String userId)
            throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyService.createTaxonomy(request, userId), ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    @Operation(summary = "2. Update Taxonomy", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy.update')")
    public ResponseEntity<MessageResponse> updateTaxonomy(
            @Valid @RequestBody UpdateTaxonomyRequest request,
            @RequestHeader(value = Constants.USER_ID, required = false) String userId)
            throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyService.updateTaxonomy(request, userId), ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/detail")
    @Operation(summary = "3. detail Taxonomy", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy.view')")
    public ResponseEntity<MessageResponse> detailTaxonomy(
            @Valid @RequestBody DetailTaxonomyRequest request,
            @RequestHeader(value = Constants.USER_ID) String userId) throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyService.findByCode(request, userId), ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);

    }

    @PostMapping(value = "/delete")
    @Operation(summary = "5. Delete Taxonomy", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy.delete')")
    public ResponseEntity<MessageResponse> deleteTaxonomies(
            @Valid @RequestBody DetailTaxonomyRequest request,
            @RequestHeader(value = Constants.USER_ID, required = false) String userId)
            throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyService.deleteByCode(request, userId),
                ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "6. Get All Taxonomy", tags = {"Taxonomy API"})
    @PreAuthorize("hasAuthority('taxonomy.view')")
    public ResponseEntity<MessageResponse> getAllTaxonomy(
            @RequestBody BaseRequest request,
            @RequestHeader(value = Constants.USER_ID, required = false) String userId)
            throws AppException {
        MessageResponse messageResponse = new MessageResponse(taxonomyService.listAllTaxonomies(request),
                ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


}