package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSegmentRiskCustomerDTO {
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String inCharge;
    private String dept;
    private String callNum;
}