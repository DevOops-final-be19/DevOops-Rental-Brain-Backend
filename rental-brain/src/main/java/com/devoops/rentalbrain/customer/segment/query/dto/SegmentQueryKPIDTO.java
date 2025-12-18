package com.devoops.rentalbrain.customer.segment.query.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SegmentQueryKPIDTO {

    //이탈 위험 고객 비중
    private Double riskCustomerRate;     // 18.3
    private Long riskCustomerCount;      // 9
    private Long totalCustomerCount;     // 전체
    private Double momChangeRate;        // 전월 대비 -2.1(%) 같은 변화율/변화값

    // 2~3번 카드들은 공통으로 들어가는 부분들 묶음

    // 이전 세그먼트 분포
    private List<DistItem> prevSegmentDist;

    // 이탈 원인 분포
    private List<DistItem> riskReasonDist;


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DistItem {
        private String name;     // "일반 고객" / "계약 만료 임박형"
        private Long count;      // 건수
        private Double rate;     // %
    }
}
