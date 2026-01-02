package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.*;

public interface CustomerSummaryAnalysisQueryService {
    CustomerSummaryAnalysisQueryKPIDTO getkpi(String month);

    CustomerSummaryAnalysisQuerySatisfactionDTO getSatisfaction();

    PageResponseDTO<CustomerSummaryAnalysisQuerySatisfactionCustomerDTO> getCustomersByStarWithPaging(int star, Criteria criteria);

    CustomerSegmentDistributionResponseDTO getSegmentDistribution();
}
