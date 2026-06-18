package com.cambofreelance.webbackend.services;

import java.util.List;
import java.util.Map;

public interface ReportService {

    List<Map<String, Object>> customerList();

    List<Map<String, Object>> activeLoans();

    List<Map<String, Object>> closedLoans();

    List<Map<String, Object>> loanPortfolio();

    List<Map<String, Object>> dailyCollection(String dateFrom, String dateTo);

    List<Map<String, Object>> monthlyCollection(Integer year);

    List<Map<String, Object>> overdueReport();

    List<Map<String, Object>> interestIncome(Integer year);

    List<Map<String, Object>> serviceFeeIncome(Integer year);

    List<Map<String, Object>> outstandingBalance();
}
