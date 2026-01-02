package com.devoops.rentalbrain.business.campaign.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Coupon {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_code", nullable = false, unique = true)
    private String couponCode;

    @Column(name = "name")
    private String name;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "content")
    private String content;

    @Column(name = "type")
    private String type;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status")
    private String status;

    @Column(name = "date_period")
    private Integer datePeriod;

    @Column(name = "min_fee")
    private Integer minFee;

    @Column(name = "max_num")
    private Integer maxNum;

    @Column(name = "segment_id")
    private Long segmentId;
}
