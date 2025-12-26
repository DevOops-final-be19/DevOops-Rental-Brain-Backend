package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerSegmentAnalysisRiskReasonCustomersListDTO {
    private String month;
    private String reasonCode;
    private int totalCount;
    private List<CustomerSegmentAnalysisRiskReasonCustomerDTO> customers;
}
