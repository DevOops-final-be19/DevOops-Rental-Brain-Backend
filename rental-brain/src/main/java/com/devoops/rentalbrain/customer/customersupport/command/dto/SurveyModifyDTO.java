package com.devoops.rentalbrain.customer.customersupport.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SurveyModifyDTO {
    private Long id;
    private String surveyCode;
    private String name;
    private String link;
    private String status;
    private String startDate;
    private String endDate;
    private Long categoryId;
}
