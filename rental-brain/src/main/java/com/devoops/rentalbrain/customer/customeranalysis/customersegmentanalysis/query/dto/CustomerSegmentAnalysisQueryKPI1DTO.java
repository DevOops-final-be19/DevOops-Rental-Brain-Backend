package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerSegmentAnalysisQueryKPI1DTO {

    // 카드 1) 이탈 위험 고객 비중
    private Double riskCustomerRate;     // %
    private Long riskCustomerCount;      // 이탈 위험 고객 수
    private Long totalCustomerCount;     // 전체 고객 수
    private Double momChangeRate;        // 전월 대비 변화 (ex. -2.1)

}
