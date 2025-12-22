package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSegmentDistributionResponseDTO {

    private long totalCustomerCount;
    private List<CustomerSegmentDistributionDTO> segments;
}
