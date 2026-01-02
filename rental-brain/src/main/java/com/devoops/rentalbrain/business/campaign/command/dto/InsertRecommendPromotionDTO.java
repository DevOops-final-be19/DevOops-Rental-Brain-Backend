package com.devoops.rentalbrain.business.campaign.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InsertRecommendPromotionDTO {
    private String name;
    private String content;
    private String segmentName;
    private Long surveyId;
}
