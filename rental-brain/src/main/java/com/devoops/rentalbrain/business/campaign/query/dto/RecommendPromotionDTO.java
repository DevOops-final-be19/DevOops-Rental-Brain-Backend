package com.devoops.rentalbrain.business.campaign.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecommendPromotionDTO {
    private Long id;
    private String name;
    private String content;
    private String segmentName;
    private Long surveyId;
    private String isUsed;
}
