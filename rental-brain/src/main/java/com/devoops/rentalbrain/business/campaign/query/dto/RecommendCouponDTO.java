package com.devoops.rentalbrain.business.campaign.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecommendCouponDTO {
    private Long id;
    private String name;
    private Integer rate;
    private String content;
    private String segmentName;
    private Long surveyId;
    private String isUsed;
}
