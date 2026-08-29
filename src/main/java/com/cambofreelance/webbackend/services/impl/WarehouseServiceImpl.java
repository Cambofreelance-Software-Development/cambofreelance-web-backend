package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.WarehouseCreateRequest;
import com.cambofreelance.webbackend.dto.response.WarehouseResponse;
import com.cambofreelance.webbackend.entities.WarehouseEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.WarehouseRepository;
import com.cambofreelance.webbackend.services.WarehouseService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "WAREHOUSE")
    public WarehouseResponse create(WarehouseCreateRequest request, String userId) {
        String code = request.getCode().trim().toUpperCase();
        if (warehouseRepository.existsByCode(code)) {
            throw new AppException("DUPLICATE_WAREHOUSE_CODE", "Warehouse code already in use: " + code);
        }

        WarehouseEntity entity = new WarehouseEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCode(code);
        entity.setName(request.getName().trim());
        entity.setAddress(request.getAddress());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setManagerName(request.getManagerName());
        entity.setIsDefault(Boolean.TRUE.equals(request.getIsDefault()));
        entity.setCreatedBy(StringUtils.hasText(userId) ? userId : Constants.SYSTEM);
        entity.setCreatedAt(new Date());
        entity.setStatus(Constants.STATUS_ACTIVE);
        warehouseRepository.save(entity);

        return WarehouseResponse.from(entity);
    }

    @Override
    public List<WarehouseResponse> getAll() {
        return warehouseRepository.findAll().stream()
            .map(WarehouseResponse::from)
            .toList();
    }

    @Override
    public WarehouseResponse getById(String warehouseId) {
        WarehouseEntity entity = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new AppException("WAREHOUSE_NOT_FOUND", "Warehouse not found: " + warehouseId));
        return WarehouseResponse.from(entity);
    }
}
