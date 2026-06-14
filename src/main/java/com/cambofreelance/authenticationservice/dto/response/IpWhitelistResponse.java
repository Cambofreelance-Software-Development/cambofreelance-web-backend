package com.cambofreelance.authenticationservice.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IpWhitelistResponse {

    private boolean enabled;
    private List<String> rules;
}
