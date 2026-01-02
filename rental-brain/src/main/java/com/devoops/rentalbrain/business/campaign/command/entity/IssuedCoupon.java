package com.devoops.rentalbrain.business.campaign.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "issued_coupon")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class IssuedCoupon {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issued_date")
    private LocalDateTime issuedDate;

    @Column(name = "is_used")
    private String isUsed;

    @Column(name = "used_date")
    private LocalDateTime usedDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "cum_id")
    private Long cumId;

    @Column(name = "con_id")
    private Long conId;
}
