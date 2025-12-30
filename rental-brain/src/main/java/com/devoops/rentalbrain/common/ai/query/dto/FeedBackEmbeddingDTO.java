package com.devoops.rentalbrain.common.ai.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FeedBackEmbeddingDTO {
    private Long id;
    private String feedBackCode;
    private String title;
    private String content;
    private Integer star;
    private String action;
    private Long cumId;
    private String categoryName;
    private Long empId;
    private Long channelId;
    private String createDate;
    private String segmentName;
}
