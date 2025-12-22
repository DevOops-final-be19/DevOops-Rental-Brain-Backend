package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.*;

import java.util.List;

public interface CustomerSummaryAnalysisQueryService {
    CustomerSummaryAnalysisQueryKPIDTO getkpi(String month);

    ChurnKpiCardResponseDTO getRiskKpi(String month);

    List<MonthlyRiskRateResponseDTO> getMonthlyRiskRate(String fromMonth,
                                                        String toMonth);

    CustomerSummaryAnalysisQuerySatisfactionDTO getSatisfaction();

    PageResponseDTO<CustomerSummaryAnalysisQuerySatisfactionCustomerDTO> getCustomersByStarWithPaging(int star, Criteria criteria);

    CustomerSegmentDistributionResponseDTO getSegmentDistribution();
}
