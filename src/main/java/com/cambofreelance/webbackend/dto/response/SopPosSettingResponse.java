package com.cambofreelance.webbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SopPosSettingResponse {
    private Boolean enabled;
    private String baseUrl;
    /** The API key itself is never returned. */
    private Boolean hasApiKey;
    /** True when the integration will actually call out (enabled + base URL + API key all present). */
    private Boolean active;
}
