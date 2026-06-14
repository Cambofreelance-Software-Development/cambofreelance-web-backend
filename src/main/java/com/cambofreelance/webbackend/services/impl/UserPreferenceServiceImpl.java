package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.dto.UserPreferencesDto;
import com.cambofreelance.webbackend.entities.UserPreferenceEntity;
import com.cambofreelance.webbackend.repository.UserPreferenceRepository;
import com.cambofreelance.webbackend.services.UserPreferenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private final UserPreferenceRepository preferenceRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UserPreferencesDto getPreferences(String userId) {
        return preferenceRepository.findById(userId)
            .map(entity -> {
                try {
                    return objectMapper.readValue(entity.getPrefsJson(), UserPreferencesDto.class);
                } catch (Exception e) {
                    log.warn("Failed to deserialize preferences for userId={}", userId, e);
                    return null;
                }
            })
            .orElse(null);
    }

    @Override
    public UserPreferencesDto savePreferences(String userId, UserPreferencesDto dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            UserPreferenceEntity entity = preferenceRepository.findById(userId)
                .orElse(UserPreferenceEntity.builder()
                    .userId(userId)
                    .createdAt(new Date())
                    .build());
            entity.setPrefsJson(json);
            entity.setUpdatedAt(new Date());
            preferenceRepository.save(entity);
            return dto;
        } catch (Exception e) {
            log.error("Failed to save preferences for userId={}", userId, e);
            throw new RuntimeException("Failed to save preferences", e);
        }
    }
}
