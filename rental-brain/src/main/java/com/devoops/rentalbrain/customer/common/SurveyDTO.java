package com.devoops.rentalbrain.customer.common;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SurveyDTO {
    private Long id;
    private String surveyCode;
    private String name;
    private String link;
    private String status;
    private String startDate;
    private String endDate;
    private String aiResponse;
    private Long categoryId;
}
