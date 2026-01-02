package com.devoops.rentalbrain.customer.segment.query.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SegmentQueryDetailDTO {

    // 기본 세그먼트
    private Long segmentId;
    private String segmentName;
    private String segmentContent;
    private Integer segmentTotalCharge;
    private int segmentPeriod;
    private boolean segmentIsContracted;
    private Integer segmentOverdued;

    //해당 세그먼트 고객 리스트(아래 클래스에서 가져오기)
    private List<SegmentCustomerDTO> customers;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class SegmentCustomerDTO {
        private Long customerId;   // 고객 ID
        private String customerCode;  // 고객코드
        private String customerName;  // 기업명
        private String inCharge;      // 담당자
        private String dept;          // 부서
        private String callNum;       // 연락처
        private Integer star;         // 평점
        private String isDeleted;     // 삭제여부(소프트딜리트)
    }
}
