package com.devoops.rentalbrain.business.campaign.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotion")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Promotion {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "promotion_code", nullable = false, unique = true)
    private String promotionCode;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

    @Column(name = "trigger_val")
    private String triggerVal;

    @Column(name = "content")
    private String content;

    @Column(name = "segment_id")
    private Long segmentId;
}
