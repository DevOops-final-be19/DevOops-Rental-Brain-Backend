package com.devoops.rentalbrain.common.ai.command.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KeywordInsightDTO {
    private List<KeywordCountDTO> inquiryKeywords;
    private List<KeywordCountDTO> positiveFeedbackKeywords;
    private List<KeywordCountDTO> complaintKeywords;
}
