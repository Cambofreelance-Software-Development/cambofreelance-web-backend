package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyDataPoint {
    private String month;
    private BigDecimal totalUsd;
    private BigDecimal totalKhr;
}
