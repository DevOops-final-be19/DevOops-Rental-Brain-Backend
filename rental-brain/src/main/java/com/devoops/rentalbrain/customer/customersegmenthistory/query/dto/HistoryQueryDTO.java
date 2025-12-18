package com.devoops.rentalbrain.customer.customersegmenthistory.query.dto;

import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeReferenceType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeTriggerType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HistoryQueryDTO {

    /*
    * 기본 적으로 고객 세그먼트 변경 이력은
    * 고객 기준으로 어덯게 바뀔지 list를 보여주는게 좋을듯
    *  */

    // 기본 테이블 셋팅
    private Long historyId;
    private Long historyPreviousSegmentId;
    private String previousSegmentName;
    private Long historyCurrentSegmentId;
    private String currentSegmentName;

    private String historyReason;
    private SegmentChangeTriggerType historyTriggerType;

    private Long historyReferenceId;
    private SegmentChangeReferenceType historyReferenceType;

    private LocalDateTime historyChangedAt;

    // customer join
    @JsonIgnore   // 지금 리스트로 뽑다가 이름 겹쳐서 일단 넣음
    private String customerName;

}
