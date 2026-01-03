package com.devoops.rentalbrain.business.campaign.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommend_promotion")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecommendPromotion {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @Column(name = "segment_name")
    private String segmentName;

    @Column(name = "survey_id")
    private Long surveyId;

    @Column(name = "is_used")
    private String isUsed;
}
