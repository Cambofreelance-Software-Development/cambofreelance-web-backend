package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

/** Body shared by the submit/approve/reject endpoints — used as approval note or rejection reason. */
@Data
public class LoanApplicationStatusActionRequest {

    private String note;
}
