package com.cambofreelance.authenticationservice.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class UserAgentParser {

    public ParsedUserAgent parse(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return ParsedUserAgent.builder()
                .browser("Unknown")
                .os("Unknown")
                .deviceType("UNKNOWN")
                .deviceName("Unknown on Unknown")
                .build();
        }

        String deviceType = parseDeviceType(userAgent);
        String browser = parseBrowser(userAgent);
        String os = parseOs(userAgent);
        String deviceName = browser + " on " + os;

        return ParsedUserAgent.builder()
            .browser(browser)
            .os(os)
            .deviceType(deviceType)
            .deviceName(deviceName)
            .build();
    }

    private String parseDeviceType(String ua) {
        if (ua.contains("Mobile") || ua.contains("Android")) {
            return "MOBILE";
        }
        if (ua.contains("Tablet") || ua.contains("iPad")) {
            return "TABLET";
        }
        return "DESKTOP";
    }

    private String parseBrowser(String ua) {
        if (ua.contains("Edg/")) {
            return "Edge";
        }
        if (ua.contains("OPR/") || ua.contains("Opera")) {
            return "Opera";
        }
        if (ua.contains("Chrome")) {
            return "Chrome";
        }
        if (ua.contains("Firefox")) {
            return "Firefox";
        }
        if (ua.contains("Safari") && !ua.contains("Chrome")) {
            return "Safari";
        }
        return "Unknown";
    }

    private String parseOs(String ua) {
        if (ua.contains("Windows")) {
            return "Windows";
        }
        if (ua.contains("Mac OS")) {
            return "macOS";
        }
        if (ua.contains("Linux")) {
            return "Linux";
        }
        if (ua.contains("Android")) {
            return "Android";
        }
        if (ua.contains("iPhone") || ua.contains("iPad")) {
            return "iOS";
        }
        return "Unknown";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParsedUserAgent {
        private String browser;
        private String os;
        private String deviceType;
        private String deviceName;
    }
}
