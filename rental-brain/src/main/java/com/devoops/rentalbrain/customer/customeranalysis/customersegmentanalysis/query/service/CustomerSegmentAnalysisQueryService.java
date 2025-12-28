package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service;


import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.*;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface CustomerSegmentAnalysisQueryService {
    CustomerSegmentAnalysisRiskKPIDTO getRiskKpi(String month);

    List<CustomerSegmentAnalysisRiskReaseonKPIDTO> getRiskReasonKpi(String month);

    List<CustomerSegmentTradeChartDTO> getSegmentTradeChart(String month);

    CustomerSegmentDetailCardDTO getSegmentDetailCard(long segmentId);

    CustomerSegmentAnalysisRiskReasonCustomersListDTO getRiskReasonCustomers(String month, String reasonCode);
}
