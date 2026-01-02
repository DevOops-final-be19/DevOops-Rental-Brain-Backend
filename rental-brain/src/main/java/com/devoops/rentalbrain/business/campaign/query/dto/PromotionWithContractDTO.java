package com.devoops.rentalbrain.business.campaign.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PromotionWithContractDTO {
    private Long id;
    private String promotionCode;
    private String name;
    private String content;
    private LocalDateTime endDate;
    private String triggerVal;
}
