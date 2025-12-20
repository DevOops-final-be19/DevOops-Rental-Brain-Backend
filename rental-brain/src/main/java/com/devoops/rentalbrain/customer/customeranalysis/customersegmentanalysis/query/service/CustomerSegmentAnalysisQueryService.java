package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service;


import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisRiskKPIDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisRiskReaseonKPIDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentDetailCardDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentTradeChartDTO;

import java.util.List;

public interface CustomerSegmentAnalysisQueryService {
    CustomerSegmentAnalysisRiskKPIDTO getRiskKpi(String month);

    List<CustomerSegmentAnalysisRiskReaseonKPIDTO> getRiskReasonKpi(String month);

    List<CustomerSegmentTradeChartDTO> getSegmentTradeChart(String month);

    CustomerSegmentDetailCardDTO getSegmentDetailCard(long segmentId);
}
