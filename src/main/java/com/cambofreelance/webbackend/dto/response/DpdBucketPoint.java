package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DpdBucketPoint {
    private String bucket;
    private long loanCount;
    private BigDecimal totalOverdueUsd;
    private BigDecimal totalOverdueKhr;
}
