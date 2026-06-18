package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDataPoint {
    private String currency;
    private long loanCount;
    private BigDecimal totalAmount;
}
