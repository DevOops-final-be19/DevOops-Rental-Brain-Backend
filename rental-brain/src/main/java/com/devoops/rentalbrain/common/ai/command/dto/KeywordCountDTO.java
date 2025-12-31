package com.devoops.rentalbrain.common.ai.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KeywordCountDTO {
    private String keyword;
    private Long count;
}
