package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.*;

import java.util.List;

public interface CustomerSegmentAnalysisQueryService {

    /* =========================
       KPI 1) 이탈 위험 KPI (월말 기준)
       ========================= */
    ChurnKpiCardResponseDTO getRiskKpi(String month);

    /* =========================
       차트) 월별 이탈 위험률 (월말 기준)
       ========================= */
    List<MonthlyRiskRateResponseDTO> getMonthlyRiskRate(String fromMonth, String toMonth);

    /* =========================
       KPI 2) 이탈 위험 사유
       ========================= */
    List<CustomerSegmentAnalysisRiskReaseonKPIDTO> getRiskReasonKpi(String month);

    CustomerSegmentAnalysisRiskReasonCustomersListDTO getRiskReasonCustomers(String month, String reasonCode);

    /* =========================
       세그먼트 분석
       ========================= */
    List<CustomerSegmentTradeChartDTO> getSegmentTradeChart(String month);

    CustomerSegmentDetailCardDTO getSegmentDetailCard(long segmentId);

    CustomerSegmentRiskCustomerPageDTO getRiskCustomersByMonth(
            String month, int page, int size);
}
