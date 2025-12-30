package com.devoops.rentalbrain.common.ai.common;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SentimentDTO {
    private String sentiment;
    private List<String> vocab;

}
