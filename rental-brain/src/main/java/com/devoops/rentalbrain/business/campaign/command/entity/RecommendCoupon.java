package com.devoops.rentalbrain.business.campaign.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommend_coupon")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecommendCoupon {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "content")
    private String content;

    @Column(name = "segment_Name")
    private String segmentName;

    @Column(name = "survey_id")
    private Long surveyId;

    @Column(name = "is_used")
    private String isUsed;
}
