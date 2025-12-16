package com.devoops.rentalbrain.customer.segment.query.dto;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import lombok.*;

import java.util.List;

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

    private List<CustomerlistCommandEntity> CustomerlistCommandEntity;
}
