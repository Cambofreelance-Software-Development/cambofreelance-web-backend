package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.PartnerPayoutEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerPayoutResponse {

    private String id;
    private String applicationId;
    private BigDecimal amount;
    private String channel;
    private String reference;
    private String note;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date paidAt;

    private String createdBy;

    public static PartnerPayoutResponse from(PartnerPayoutEntity e) {
        return PartnerPayoutResponse.builder()
            .id(e.getId())
            .applicationId(e.getApplicationId())
            .amount(e.getAmount())
            .channel(e.getChannel())
            .reference(e.getReference())
            .note(e.getNote())
            .paidAt(e.getPaidAt())
            .createdBy(e.getCreatedBy())
            .build();
    }
}
