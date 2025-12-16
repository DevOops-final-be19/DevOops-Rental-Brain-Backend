package com.devoops.rentalbrain.product.maintenance.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AfterServiceSummaryDTO {
    private int thisMonthScheduleCount;
    private int imminent72hCount;
    private int thisMonthCompletedCount;
    private int todayInProgressCount;
}
