package com.devoops.rentalbrain.customer.segment.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SegmentQueryKPIDTO {

    private double totalChurnRate;   // 전체 이탈률 (임시/추정 가능)
    private double momChangeRate;    // 전월 대비 증감
    private double riskCustomerRate; // 이탈 위험 고객 비중
    // 피그마에 카드 하나 더있는데 그건 각가 다른곳에서 불러오기

}
