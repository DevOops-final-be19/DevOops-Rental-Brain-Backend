package com.devoops.rentalbrain.dashboard.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MonthlyRevenueKpiDTO {

    private String month;       // YYYY-MM
    private String prevMonth;   // YYYY-MM

    private long curRevenue;    // 이번달 월매출(월 과금 합)
    private long prevRevenue;   // 전월 월매출

    private long momDiff;       // cur - prev
    private double momRate;     // (cur-prev)/prev *100 (prev=0이면 0)
}