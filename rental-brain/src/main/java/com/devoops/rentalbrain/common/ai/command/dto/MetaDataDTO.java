package com.devoops.rentalbrain.common.ai.command.dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MetaDataDTO {
    private String category;        // 제품 불량, 서비스 만족
    private String sentiment;       // 긍정, 부정
    private List<String> vocab;     // 키워드
    private String segments;        // VIP 고객 등
    private String responseStyle;   // summary | list | explain

    public Map<String, Object> toFilterMap() {
        Map<String, Object> filter = new HashMap<>();

        if (category != null && !category.isBlank()) {
            filter.put("category", category);
        }

        if (sentiment != null && !sentiment.isBlank()) {
            filter.put("sentiment", sentiment);
        }

        if (segments != null && !segments.isBlank()) {
            filter.put("segments", segments);
        }

        // vocab은 보통 "terms filter" 또는 후처리용
        if (vocab != null && !vocab.isEmpty()) {
            filter.put("vocab", vocab);
        }

        if (responseStyle != null && !responseStyle.isBlank()) {
            filter.put("responseStyle", responseStyle);
        }

        return filter;
    }
}