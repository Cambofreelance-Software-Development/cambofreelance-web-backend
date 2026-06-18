package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepaymentInstallmentResponse {

    private int installmentNo;
    private Date dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalPayment;
    private BigDecimal remainingBalance;
}
