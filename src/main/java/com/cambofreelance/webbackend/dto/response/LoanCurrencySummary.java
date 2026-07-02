package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanCurrencySummary {
    private String currency;
    private long activeLoans;
    private long totalApplications;
    private BigDecimal portfolioAmount;
    private BigDecimal outstandingBalance;
    private BigDecimal todayCollections;
}
