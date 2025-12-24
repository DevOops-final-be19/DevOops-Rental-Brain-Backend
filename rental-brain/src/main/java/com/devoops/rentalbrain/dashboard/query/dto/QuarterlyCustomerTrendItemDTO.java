package com.devoops.rentalbrain.dashboard.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuarterlyCustomerTrendItemDTO {
    private String quarter;          // Q1 ~ Q4
    private long totalCustomerCount; // 분기말 기준 누적 고객(첫 계약일 <= 분기말)
    private long newCustomerCount;   // 분기 내 신규(첫 계약일이 분기 범위)
}
