package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.response.PersistedScheduleResponse;

public interface RepaymentScheduleExportService {

    byte[] toXlsx(PersistedScheduleResponse schedule);

    byte[] toDocx(PersistedScheduleResponse schedule);

    byte[] toPdf(PersistedScheduleResponse schedule);
}
