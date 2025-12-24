package com.devoops.rentalbrain.dashboard.query.service;

import com.devoops.rentalbrain.dashboard.query.dto.DashboardKpiResponseDTO;
import com.devoops.rentalbrain.dashboard.query.dto.QuarterlyCustomerTrendResponseDTO;

public interface DashboardCustomerQueryService {
    QuarterlyCustomerTrendResponseDTO getQuarterlyTrend(Integer year);

    DashboardKpiResponseDTO getDashboardKpi(String month);
}
