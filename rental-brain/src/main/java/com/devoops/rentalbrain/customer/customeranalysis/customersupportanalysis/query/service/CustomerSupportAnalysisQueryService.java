package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.CustomerSupportAnalysisMonthlyTrendResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.CustomerSupportAnalysisQueryResponseKPIDTO;

public interface CustomerSupportAnalysisQueryService {
    CustomerSupportAnalysisQueryResponseKPIDTO getKpi(String month);

    // 응대 월별 트랜드 차트
    CustomerSupportAnalysisMonthlyTrendResponseDTO getMonthlyTrend(int year);
}

