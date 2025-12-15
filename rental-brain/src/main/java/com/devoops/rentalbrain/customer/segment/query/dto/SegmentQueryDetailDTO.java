package com.devoops.rentalbrain.customer.segment.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SegmentQueryDetailDTO {

    // table segment
    private Long segmentId;
    private String segmentName;
    private String segmentContent;
    private Integer segmentTotalCharge;
    private int segmentPeriod;
    private boolean segmentIsContracted;
    private Integer segmentOverdued;

}
