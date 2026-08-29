package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.response.AttributeResponse;
import java.util.List;

public interface AttributeCatalogService {

    List<AttributeResponse> getAttributesByCategory(String category);

    List<AttributeResponse> getVariantAttributes();
}
