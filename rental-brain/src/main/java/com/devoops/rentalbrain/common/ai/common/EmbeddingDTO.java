package com.devoops.rentalbrain.common.ai.common;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmbeddingDTO {
    private String chunkId;
    private String text;
    private String source;
    private String sourceId;
    private Long customerId;
    private String segments;
    private String sentiment = null;
    private Integer score = null;
    private String category;
    private Integer priority = null;
    private String status = null;
    private List<String> vocab = null;
    private String createAt;
}
