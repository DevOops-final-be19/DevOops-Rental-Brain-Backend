package com.devoops.rentalbrain.business.campaign.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CouponDTO {
    private Long id;
    private String couponCode;
    private String name;
    private Integer rate;
    private String type;
    private Integer minFee;
    private String segmentName;
    private String status;   // 컬럼에 없음(쿼리문으로 계산해서 값 넣기): AS STATUS
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer datePeriod;
    private String content;
    private Integer issued; // 발급받은 수 (issued_coupon에서 join 후 count)
    private Integer used; // 사용된 쿠폰 수 (issued_coupon에서 join 후 count)
    private Integer usedRate; // 사용률 = used / issued * 100
}
