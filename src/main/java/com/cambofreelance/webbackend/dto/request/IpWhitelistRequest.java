package com.cambofreelance.webbackend.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class IpWhitelistRequest {

    private boolean enabled;
    private List<String> rules; // IPs and CIDRs, e.g. "192.168.1.1", "10.0.0.0/24"
}
