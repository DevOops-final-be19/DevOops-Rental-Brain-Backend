package com.devoops.rentalbrain.dashboard.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardKpiResponseDTO {

    private String month;                 // YYYY-MM
    private int expiringContractCount;    // 만료 임박(D-60)
    private int payOverdueCount;          // 납부 연체(진행중)
    private int waitingInquiryCount;      // 문의 대기

    private long mtdRevenue;              // 이번 달 매출(합)
    private double momRevenueRate;        // 전월 대비 매출 증감률(%)
}