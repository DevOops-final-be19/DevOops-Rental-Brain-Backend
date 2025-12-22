package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class CustomerSupportAnalysisQueryTypeCountDTO {

    private String type; // QUOTE / INQUIRY / FEEDBACK

    private long currentCount;  // 이번 달
    private long previousCount; // 전월
    private long deltaCount;    // 증감(이번-전월)

    private double momPercent;  // 증감률(%) (전월=0이면 null 또는 0)
}
