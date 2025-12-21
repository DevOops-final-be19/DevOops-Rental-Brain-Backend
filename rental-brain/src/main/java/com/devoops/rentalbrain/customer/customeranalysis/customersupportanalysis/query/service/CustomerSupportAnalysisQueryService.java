package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.CustomerSupportAnalysisQueryResponseKPIDTO;

public interface CustomerSupportAnalysisQueryService {
    CustomerSupportAnalysisQueryResponseKPIDTO getKpi(String month);
}
