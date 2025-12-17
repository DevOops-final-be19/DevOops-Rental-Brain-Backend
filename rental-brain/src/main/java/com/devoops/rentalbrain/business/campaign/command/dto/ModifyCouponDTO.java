package com.devoops.rentalbrain.business.campaign.command.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ModifyCouponDTO {
    private String name;
    private Integer rate;
    private String content;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer datePeriod;
    private Integer minFee;
    private Integer maxNum;
    private String segmentName;
}
