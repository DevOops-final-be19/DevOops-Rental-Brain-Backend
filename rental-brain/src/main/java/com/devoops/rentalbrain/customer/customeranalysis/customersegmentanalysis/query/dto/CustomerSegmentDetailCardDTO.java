package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class CustomerSegmentDetailCardDTO {

    private Long segmentId;
    private String segmentName;

    private int customerCount;

    private long totalTradeAmount;
    private double avgTradeAmount;

    private double avgSatisfaction;    // 피드백 만족도 평균

    private String topItemName;         // 인기 품목 (없으면 "-")

    private String topSupport;          // 주요 문의사항 (없으면 "-")
    private String topFeedback;         // 주요 피드백 (없으면 "-")
}
