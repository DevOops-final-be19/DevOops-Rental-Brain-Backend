package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomerSegmentDistributionDTO {

    private Long segmentId;
    private String segmentName;
    private Long customerCount;
    private double countPercent;
}