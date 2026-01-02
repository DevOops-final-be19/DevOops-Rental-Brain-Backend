package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSegmentRiskCustomerPageDTO {
    private String month;
    private int totalCount;
    private List<CustomerSegmentRiskCustomerDTO> customers;
}