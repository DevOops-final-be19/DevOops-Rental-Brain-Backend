package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="survey")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerSummaryAnalysisSurveyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "survey_code", nullable = false, unique = true)
    private String surveyCode;
    @Column
    private String name;
    @Column
    private String link;
    @Column
    private String status;
    @Column
    private String startDate;
    @Column
    private String endDate;
    @Column
    private String aiResponse;
    @Column
    private Long categoryId;
}
