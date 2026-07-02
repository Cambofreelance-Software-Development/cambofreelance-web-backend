package com.cambofreelance.webbackend.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private DashboardSummaryResponse summary;
    private List<LoanCurrencySummary> loansByCurrency;
    private List<MonthlyDataPoint> monthlyDisbursement;
    private List<MonthlyDataPoint> collectionTrend;
    private List<CurrencyDataPoint> portfolioByCurrency;
    private List<DpdBucketPoint> overdueAnalysis;
}
