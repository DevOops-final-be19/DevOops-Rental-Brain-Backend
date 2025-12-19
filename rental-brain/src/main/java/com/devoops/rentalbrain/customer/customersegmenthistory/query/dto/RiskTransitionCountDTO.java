package com.devoops.rentalbrain.customer.customersegmenthistory.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RiskTransitionCountDTO {

    private Long customerId;

    // customer join
    private String customerName;
    private int churnCount;
    private int blacklistCount;
}
