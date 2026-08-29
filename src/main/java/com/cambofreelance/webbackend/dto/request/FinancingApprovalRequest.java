package com.cambofreelance.webbackend.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class FinancingApprovalRequest {
    private BigDecimal approvedAmount;
    private String externalReference;
    private String approvalNote;
}
