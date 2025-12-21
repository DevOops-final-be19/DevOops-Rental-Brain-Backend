package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class CustomerSupportAnalysisQueryEfficiencyKPIDTO {


    // 처리 완료율
    private double completionRate;     // 이번 달 (%)
    private double completionRateMomP; // 전월 대비 (%p)

    // 평균 응대 시간(견적 processing_time 기준)
    private double avgResponseTime;    // 이번 달 (분 or 시간)
    private double avgResponseTimeMomPercent; // 전월 대비 (%)

}
