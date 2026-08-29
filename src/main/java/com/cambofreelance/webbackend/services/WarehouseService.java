package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.WarehouseCreateRequest;
import com.cambofreelance.webbackend.dto.response.WarehouseResponse;
import java.util.List;

public interface WarehouseService {

    WarehouseResponse create(WarehouseCreateRequest request, String userId);

    List<WarehouseResponse> getAll();

    WarehouseResponse getById(String warehouseId);
}
