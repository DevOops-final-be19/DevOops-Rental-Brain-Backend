package com.devoops.rentalbrain.common.ai.common;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WordDTO {
    private Long id;
    private String keywordText;
    private String createdAt;
}
