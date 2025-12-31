package com.devoops.rentalbrain.common.ai.command.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KeywordsDTO {
    List<String> keywords;
}
