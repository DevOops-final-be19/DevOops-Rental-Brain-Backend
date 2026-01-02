package com.devoops.rentalbrain.dashboard.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardKpiResponseDTO {


    private String month;                 // YYYY-MM (기준 월)

    // 운영 현황 KPI
    private int expiringContractCount;    // 만료 임박(D-60)
    private int payOverdueCount;          // 납부 연체(진행중)
    private int waitingInquiryCount;      // 문의 대기

    // 매출 KPI (월 과금 기준, MRR)
    private long mtdRevenue;              // 이번 달 매출(월 과금 합)
    private long prevMtdRevenue;          // 전월 매출(월 과금 합)
    private long momRevenueDiff;          // 전월 대비 증감액 (이번달 - 전월)
    private double momRevenueRate;        // 전월 대비 증감률(%)
}