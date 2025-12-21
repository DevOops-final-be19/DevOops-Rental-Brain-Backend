package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class CustomerSupportAnalysisQueryKPICardDTO {


    private String title;      // "총 응대 건수"
    private String subtitle;   // "전월 대비 변화 (유형별)"

    private long ytdTotal;     // 올해 누적 전체
    private long ytdQuote;     // 올해 누적 견적
    private long ytdInquiry;   // 올해 누적 문의
    private long ytdFeedback;  // 올해 누적 피드백

    private List<CustomerSupportAnalysisQueryTypeCountDTO> typeStats;

}
