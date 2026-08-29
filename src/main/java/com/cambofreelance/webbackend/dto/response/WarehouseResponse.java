package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.WarehouseEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseResponse {

    private String id;
    private String code;
    private String name;
    private String address;
    private String phoneNumber;
    private String managerName;
    private Boolean isDefault;
    private String status;
    private Date createdAt;
    private String createdBy;

    public static WarehouseResponse from(WarehouseEntity e) {
        return WarehouseResponse.builder()
            .id(e.getId())
            .code(e.getCode())
            .name(e.getName())
            .address(e.getAddress())
            .phoneNumber(e.getPhoneNumber())
            .managerName(e.getManagerName())
            .isDefault(e.getIsDefault())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .build();
    }
}
