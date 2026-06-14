package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.UserPreferencesDto;

public interface UserPreferenceService {

    /** Returns the stored preferences for the user, or null if none have been saved yet. */
    UserPreferencesDto getPreferences(String userId);

    /** Creates or fully replaces the stored preferences for the user. */
    UserPreferencesDto savePreferences(String userId, UserPreferencesDto dto);
}
