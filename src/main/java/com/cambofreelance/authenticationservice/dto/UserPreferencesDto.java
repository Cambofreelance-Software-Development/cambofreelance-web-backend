package com.cambofreelance.authenticationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPreferencesDto {

    /** next-themes value: "light" | "dark" | "system" | "high-contrast" */
    private String theme;

    private ColorPrefs color;
    private LayoutPrefs layout;
    private AccessibilityPrefs accessibility;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ColorPrefs {
        private String primary;
        private String secondary;
        private String accent;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LayoutPrefs {
        private Boolean sidebarCollapsed;
        private String  navStyle;       // "sidebar" | "topbar"
        private String  menuPosition;   // "left" | "right"
        private Boolean compactMode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AccessibilityPrefs {
        private String  fontSize;        // "small" | "medium" | "large" | "xlarge"
        private Boolean highContrast;
        private Boolean reducedMotion;
        private Boolean screenReader;
    }
}
