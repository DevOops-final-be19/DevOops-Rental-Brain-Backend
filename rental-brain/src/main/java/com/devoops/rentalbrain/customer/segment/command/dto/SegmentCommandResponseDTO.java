package com.devoops.rentalbrain.customer.segment.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SegmentCommandResponseDTO {

    private Long segmentId;
    private String segmentName;
    private String segmentContent;
    private Long segmentTotalCharge;     // Entity랑 타입 맞추기 (Long)
    private Integer segmentPeriod;       // null 가능하게 (int -> Integer 추천)
    private Boolean segmentIsContracted; // null 가능하게 (boolean -> Boolean 추천)
    private Integer segmentOverdued;


}
