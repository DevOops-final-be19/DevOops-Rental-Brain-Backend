package com.devoops.rentalbrain.product.maintenance.query.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NextWeekScheduleDTO {

    private LocalDate scheduleDate;
    private String dayOfWeek;
    private Integer totalCount;
    private String summary;
}
