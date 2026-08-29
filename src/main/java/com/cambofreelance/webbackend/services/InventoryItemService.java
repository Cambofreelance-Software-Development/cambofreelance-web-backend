package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.InventoryItemCreateRequest;
import com.cambofreelance.webbackend.dto.response.InventoryItemResponse;
import com.cambofreelance.webbackend.dto.response.VehicleTimelineItemResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface InventoryItemService {

    InventoryItemResponse create(InventoryItemCreateRequest request, String userId);

    InventoryItemResponse getById(String itemId);

    Page<InventoryItemResponse> search(
        String search,
        String variantId,
        String warehouseId,
        String itemStatus,
        int page,
        int size
    );

    List<VehicleTimelineItemResponse> getTimeline(String itemId);
}
