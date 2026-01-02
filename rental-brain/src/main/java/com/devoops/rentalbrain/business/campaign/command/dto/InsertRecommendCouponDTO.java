package com.devoops.rentalbrain.business.campaign.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InsertRecommendCouponDTO {
    private String name;
    private Integer rate;
    private String content;
    private String segmentName;
    private Long surveyId;
}
