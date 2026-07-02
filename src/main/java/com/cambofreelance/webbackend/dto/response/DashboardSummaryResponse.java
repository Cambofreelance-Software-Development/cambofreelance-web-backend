package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardSummaryResponse {
    private long totalCustomers;
    private long totalActiveLoans;
    private BigDecimal totalLoanPortfolio;
    private BigDecimal todayCollectionsUsd;
    private BigDecimal todayCollectionsKhr;
    private long overdueLoans;
    private BigDecimal outstandingBalance;
}
