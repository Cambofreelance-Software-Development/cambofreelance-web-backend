package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

/** Body shared by the verify/approve/reject endpoints — reused as verification/approval note or rejection reason. */
@Data
public class CustomerStatusActionRequest {

    private String note;
}
