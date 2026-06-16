package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

@Data
public class TenantUserUpdateRequest {

    private String email;

    private String phoneNumber;

    private String password;
}
