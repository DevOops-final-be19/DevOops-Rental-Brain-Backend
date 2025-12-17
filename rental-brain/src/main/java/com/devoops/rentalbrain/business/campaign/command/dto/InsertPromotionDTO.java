package com.devoops.rentalbrain.business.campaign.command.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InsertPromotionDTO {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String type;
    private String triggerVal;
    private String content;
    private String segmentName;
}
