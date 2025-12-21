package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.ChurnKpiCardResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.CustomerSummaryAnalysisQuerySatisfactionDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.MonthlyRiskRateResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.CustomerSummaryAnalysisQueryKPIDTO;

import java.util.List;

public interface CustomerSummaryAnalysisQueryService {
    CustomerSummaryAnalysisQueryKPIDTO getkpi(String month);

    ChurnKpiCardResponseDTO getRiskKpi(String month);

    List<MonthlyRiskRateResponseDTO> getMonthlyRiskRate(String fromMonth,
                                                        String toMonth);

    CustomerSummaryAnalysisQuerySatisfactionDTO getSatisfaction();

}
