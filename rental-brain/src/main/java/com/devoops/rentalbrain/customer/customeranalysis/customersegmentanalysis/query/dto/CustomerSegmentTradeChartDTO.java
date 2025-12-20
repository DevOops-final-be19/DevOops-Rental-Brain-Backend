package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class CustomerSegmentTradeChartDTO {
    private Long segmentId;
    private String segmentName;

    private int customerCount;
    private long totalTradeAmount;
    private double avgTradeAmount;
}
