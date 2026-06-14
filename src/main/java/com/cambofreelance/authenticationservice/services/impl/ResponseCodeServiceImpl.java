package com.cambofreelance.authenticationservice.services.impl;

import com.cambofreelance.authenticationservice.audit.Auditable;
import com.cambofreelance.authenticationservice.caches.ResponseCodeRedisCache;
import com.cambofreelance.authenticationservice.caches.ResponseManagerCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.ResponseCodeDto;
import com.cambofreelance.authenticationservice.dto.request.ResponseCodeRequest;
import com.cambofreelance.authenticationservice.dto.response.ResponseCodeResponse;
import com.cambofreelance.authenticationservice.entities.ResponseCodeEntity;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import com.cambofreelance.authenticationservice.repository.ResponseCodeRepository;
import com.cambofreelance.authenticationservice.services.ResponseCodeService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponseCodeServiceImpl implements ResponseCodeService {

    private final ResponseCodeRepository repository;
    private final ResponseCodeRedisCache redisCache;

    @Override
    public Page<ResponseCodeResponse> list(String search, String type, String status, String serviceType, int page, int size) {
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        String typeParam = (type != null && !type.isBlank()) ? type.trim() : null;
        String statusParam = (status != null && !status.isBlank()) ? status.trim() : null;
        String serviceTypeParam = (serviceType != null && !serviceType.isBlank()) ? serviceType.trim() : null;

        return repository.search(searchParam, typeParam, statusParam, serviceTypeParam,
                PageRequest.of(page, size, Sort.by("code").ascending()))
            .map(ResponseCodeResponse::from);
    }

    @Override
    public ResponseCodeResponse getById(Long id) {
        return ResponseCodeResponse.from(findActiveById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "RESPONSE_CODE")
    public ResponseCodeResponse create(ResponseCodeRequest request) {
        String code = request.getCode().trim().toUpperCase();

        if (repository.existsByCodeAndStatusNot(code, Constants.STATUS_DELETE)) {
            AppException ex = new AppException("DUPLICATE_CODE", "Response code '" + code + "' already exists");
            ex.setHttpStatus(HttpStatus.CONFLICT);
            throw ex;
        }

        ResponseCodeEntity entity = new ResponseCodeEntity();
        entity.setCode(code);
        entity.setDescription(request.getDescription());
        entity.setHttpStatus(request.getHttpStatus());
        entity.setKey(request.getKey());
        entity.setType(request.getType());
        entity.setMessageEn(request.getMessageEn());
        entity.setMessageKm(request.getMessageKm());
        entity.setMessageCn(request.getMessageCn());
        entity.setServiceType(request.getServiceType());
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setCreatedAt(new Date());
        entity.setCreatedBy(Constants.SYSTEM);

        ResponseCodeEntity saved = repository.save(entity);
        syncToCache(saved);
        return ResponseCodeResponse.from(saved);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "RESPONSE_CODE", entityClass = ResponseCodeEntity.class)
    public ResponseCodeResponse update(Long id, ResponseCodeRequest request) {
        ResponseCodeEntity entity = findActiveById(id);

        String newCode = request.getCode().trim().toUpperCase();
        if (!newCode.equals(entity.getCode()) && repository.existsByCodeAndStatusNot(newCode, Constants.STATUS_DELETE)) {
            AppException ex = new AppException("DUPLICATE_CODE", "Response code '" + newCode + "' already exists");
            ex.setHttpStatus(HttpStatus.CONFLICT);
            throw ex;
        }

        String oldCode = entity.getCode();

        entity.setCode(newCode);
        entity.setDescription(request.getDescription());
        entity.setHttpStatus(request.getHttpStatus());
        entity.setKey(request.getKey());
        entity.setType(request.getType());
        entity.setMessageEn(request.getMessageEn());
        entity.setMessageKm(request.getMessageKm());
        entity.setMessageCn(request.getMessageCn());
        entity.setServiceType(request.getServiceType());
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy(Constants.SYSTEM);

        ResponseCodeEntity saved = repository.save(entity);

        if (!oldCode.equals(newCode)) {
            redisCache.deleteRespCode(oldCode);
            ResponseManagerCache.removeRespCode(oldCode);
        }
        syncToCache(saved);
        return ResponseCodeResponse.from(saved);
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "RESPONSE_CODE", entityClass = ResponseCodeEntity.class)
    public void delete(Long id) {
        ResponseCodeEntity entity = findActiveById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setDeletedAt(new Date());
        entity.setDeletedBy(Constants.SYSTEM);
        repository.save(entity);

        redisCache.deleteRespCode(entity.getCode());
        ResponseManagerCache.removeRespCode(entity.getCode());
    }

    @Override
    public void reloadCache() {
        List<ResponseCodeEntity> all = repository.findByStatus(Constants.STATUS_ACTIVE);
        List<ResponseCodeDto> dtos = all.stream().map(this::toDto).toList();
        redisCache.initRespCodeCache(dtos);
        ResponseManagerCache.initRespCodeCache(dtos);
        log.info("Reloaded response code cache with {} entries", dtos.size());
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private ResponseCodeEntity findActiveById(Long id) {
        return repository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("NOT_FOUND", "Response code not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }

    private void syncToCache(ResponseCodeEntity entity) {
        ResponseCodeDto dto = toDto(entity);
        redisCache.addRespCodeCache(dto);
        ResponseManagerCache.putRespCode(dto);
    }

    private ResponseCodeDto toDto(ResponseCodeEntity e) {
        ResponseCodeDto dto = new ResponseCodeDto();
        dto.setId(e.getId());
        dto.setCode(e.getCode());
        dto.setHttpStatus(e.getHttpStatus());
        dto.setKey(e.getKey());
        dto.setType(e.getType());
        dto.setDescription(e.getDescription());
        dto.setMessage(e.getMessageEn());
        dto.setMessageKm(e.getMessageKm());
        dto.setMessageCn(e.getMessageCn());
        dto.setStatus(e.getStatus());
        return dto;
    }
}
