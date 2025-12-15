package com.devoops.rentalbrain.customer.segment.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SegmentQueryListDTO {

    // table segment
    // 기본 List 는 id, name, Content 까지만
    private Long segmentId;
    private String segmentName;
    private String segmentContent;


}
