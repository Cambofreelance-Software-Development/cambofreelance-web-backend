package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.RepaymentInstallmentUpdateRequest;
import com.cambofreelance.webbackend.dto.response.InstallmentResponse;
import com.cambofreelance.webbackend.dto.response.PersistedScheduleResponse;
import java.util.List;

public interface RepaymentScheduleService {

    /** Persist a new repayment schedule for a loan. Fails if one already exists. */
    PersistedScheduleResponse generate(String loanApplicationId, String userId);

    /** Delete and regenerate the schedule. Fails if any installment is PAID or PARTIAL_PAID. */
    PersistedScheduleResponse recalculate(String loanApplicationId, String userId);

    /** Return the persisted schedule with all installments. */
    PersistedScheduleResponse getSchedule(String loanApplicationId);

    /** Update a single installment (status, paidAmount, paidDate, notes). */
    InstallmentResponse updateInstallment(String installmentId, RepaymentInstallmentUpdateRequest request, String userId);

    /** Mark all PENDING installments past their due date as OVERDUE. Returns updated count. */
    int markOverdue(String loanApplicationId, String userId);
}
