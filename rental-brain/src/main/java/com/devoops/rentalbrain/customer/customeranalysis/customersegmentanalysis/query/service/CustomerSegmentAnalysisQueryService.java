package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisQueryKPI1DTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisQueryKPI2DTO;

public interface CustomerSegmentAnalysisQueryService {
    // 세그먼트 kpi 조회
    CustomerSegmentAnalysisQueryKPI1DTO selectCustomerSegmentAnalysisKpi1(String month);

    CustomerSegmentAnalysisQueryKPI2DTO selectCustomerSegmentAnalysisKpi2(String month);

}
