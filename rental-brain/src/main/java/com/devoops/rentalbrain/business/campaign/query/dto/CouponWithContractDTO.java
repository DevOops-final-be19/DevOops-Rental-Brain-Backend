package com.devoops.rentalbrain.business.campaign.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CouponWithContractDTO {
    private String couponCode;
    private String name;
    private String content;
    private Integer rate;
    private LocalDateTime endDate;
}
