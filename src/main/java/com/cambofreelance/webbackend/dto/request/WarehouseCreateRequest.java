package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WarehouseCreateRequest {

    @NotBlank(message = "Warehouse code is required")
    private String code;

    @NotBlank(message = "Warehouse name is required")
    private String name;

    private String address;
    private String phoneNumber;
    private String managerName;
    private Boolean isDefault = false;
}
