package com.devoops.rentalbrain.customer.customersupport.query.dto;

import com.devoops.rentalbrain.customer.common.SurveyCategoryDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SurveyAndCategoryDTO {
    private Long id;
    private String surveyCode;
    private String name;
    private String link;
    private String status;
    private String startDate;
    private String endDate;
    private SurveyCategoryDTO surveyCategoryDTO;
}
