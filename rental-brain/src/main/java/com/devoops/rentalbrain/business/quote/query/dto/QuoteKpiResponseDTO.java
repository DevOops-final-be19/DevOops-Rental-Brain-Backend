package com.devoops.rentalbrain.business.quote.query.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuoteKpiResponseDTO {

    // 전체 상담 건수
    private Long totalCount;

    // 오늘 완료 상담 건수 (오늘 상담일시 기준)
    private Long todayCount;

    // 전체 평균 처리시간(분)
    private Double avgProcessingAll;

    // 최근 7일 평균 처리시간(분)
    private Double avgProcessing7Days;
}
