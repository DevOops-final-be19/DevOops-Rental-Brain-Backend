package com.devoops.rentalbrain.business.campaign.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PromotionDTO {
    private Long id;
    private String promotionCode;
    private String name;
    private String type;
    private String status;
    private String triggerVal;
    private String segmentName;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
