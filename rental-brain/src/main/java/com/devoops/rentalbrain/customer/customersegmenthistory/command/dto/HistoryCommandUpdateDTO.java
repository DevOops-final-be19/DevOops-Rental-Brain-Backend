package com.devoops.rentalbrain.customer.customersegmenthistory.command.dto;

import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeReferenceType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeTriggerType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class HistoryCommandUpdateDTO {
    private String reason;
    private SegmentChangeTriggerType triggerType;
    private SegmentChangeReferenceType referenceType;
    private Long referenceId;
}
