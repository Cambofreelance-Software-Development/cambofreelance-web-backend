package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepaymentScheduleResponse {

    private String loanApplicationId;
    private String loanNumber;
    private String customerCode;
    private String currency;

    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private BigDecimal serviceFeeRate;
    private BigDecimal interestAmount;
    private BigDecimal serviceFeeAmount;
    private BigDecimal netDisbursement;
    /** loanAmount + interestAmount — the total the borrower repays over all installments. */
    private BigDecimal totalRepayment;

    private int numberOfPayments;
    private String paymentType;
    private Date firstPaymentDate;

    private List<RepaymentInstallmentResponse> schedule;
}
