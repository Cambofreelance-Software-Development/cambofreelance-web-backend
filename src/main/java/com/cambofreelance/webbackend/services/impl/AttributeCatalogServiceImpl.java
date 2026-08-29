package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.dto.response.AttributeResponse;
import com.cambofreelance.webbackend.entities.AttributeEntity;
import com.cambofreelance.webbackend.entities.AttributeValueEntity;
import com.cambofreelance.webbackend.repository.AttributeRepository;
import com.cambofreelance.webbackend.repository.AttributeValueRepository;
import com.cambofreelance.webbackend.services.AttributeCatalogService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttributeCatalogServiceImpl implements AttributeCatalogService {

    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;

    @Override
    public List<AttributeResponse> getAttributesByCategory(String category) {
        List<AttributeEntity> attrs = attributeRepository
            .findByApplicableCategoryOrApplicableCategoryIsNullOrderBySortOrderAsc(category);

        List<String> attrIds = attrs.stream().map(AttributeEntity::getId).toList();
        Map<String, List<AttributeValueEntity>> valMap = attributeValueRepository
            .findByAttributeIdInOrderBySortOrderAsc(attrIds)
            .stream()
            .collect(Collectors.groupingBy(AttributeValueEntity::getAttributeId));

        List<AttributeResponse> result = new ArrayList<>();
        for (AttributeEntity a : attrs) {
            result.add(AttributeResponse.from(a, valMap.getOrDefault(a.getId(), List.of())));
        }
        return result;
    }

    @Override
    public List<AttributeResponse> getVariantAttributes() {
        List<AttributeEntity> attrs = attributeRepository.findByIsVariantAttributeTrueOrderBySortOrderAsc();
        List<String> attrIds = attrs.stream().map(AttributeEntity::getId).toList();
        Map<String, List<AttributeValueEntity>> valMap = attributeValueRepository
            .findByAttributeIdInOrderBySortOrderAsc(attrIds)
            .stream()
            .collect(Collectors.groupingBy(AttributeValueEntity::getAttributeId));

        List<AttributeResponse> result = new ArrayList<>();
        for (AttributeEntity a : attrs) {
            result.add(AttributeResponse.from(a, valMap.getOrDefault(a.getId(), List.of())));
        }
        return result;
    }
}
