package com.devoops.rentalbrain.business.campaign.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotion_log")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PromotionLog {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "participation_date")
    private LocalDateTime participationDate;

    @Column(name = "cum_id")
    private Long cumId;

    @Column(name = "promotion_id")
    private Long promotionId;
}
