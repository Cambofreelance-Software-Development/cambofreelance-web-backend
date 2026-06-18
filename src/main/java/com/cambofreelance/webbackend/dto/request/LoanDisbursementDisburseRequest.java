package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Data;

@Data
public class LoanDisbursementDisburseRequest {

    @NotNull(message = "Disbursement date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date disbursementDate;

    private String notes;
}
