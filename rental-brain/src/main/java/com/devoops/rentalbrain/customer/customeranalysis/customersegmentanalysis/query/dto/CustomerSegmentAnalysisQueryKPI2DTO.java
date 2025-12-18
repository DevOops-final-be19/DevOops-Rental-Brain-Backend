package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerSegmentAnalysisQueryKPI2DTO {

    // 카드 2) 이탈 위험 사유 분포 (원인별 비중)
    private List<DistItem> riskReasonDist;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class DistItem {
        private String name;     // "계약 만료 임박형" ...
        private Long count;      // 건수
        private Double rate;     // %
    }
}
