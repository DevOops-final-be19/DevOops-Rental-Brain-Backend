package com.devoops.rentalbrain.customer.segment.command.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SegmentCommandCreateDTO {
    private Long segmentId;
    private String segmentName;
    private String segmentContent;
    private Long segmentTotalCharge;     // ⭐ Entity랑 타입 맞추기 (Long)
    private Integer segmentPeriod;       // ⭐ null 가능하게 (int -> Integer 추천)
    private Boolean segmentIsContracted; // ⭐ null 가능하게 (boolean -> Boolean 추천)
    private Integer segmentOverdued;

    // 생성 응답용(최소)
    public SegmentCommandCreateDTO(Long segmentId, String segmentName, String segmentContent) {
        this.segmentId = segmentId;
        this.segmentName = segmentName;
        this.segmentContent = segmentContent;
    }

    // 필요하면 전체 생성자도 추가
    public SegmentCommandCreateDTO(Long segmentId, String segmentName, String segmentContent,
                                   Long segmentTotalCharge, Integer segmentPeriod,
                                   Boolean segmentIsContracted, Integer segmentOverdued) {
        this.segmentId = segmentId;
        this.segmentName = segmentName;
        this.segmentContent = segmentContent;
        this.segmentTotalCharge = segmentTotalCharge;
        this.segmentPeriod = segmentPeriod;
        this.segmentIsContracted = segmentIsContracted;
        this.segmentOverdued = segmentOverdued;
    }
}