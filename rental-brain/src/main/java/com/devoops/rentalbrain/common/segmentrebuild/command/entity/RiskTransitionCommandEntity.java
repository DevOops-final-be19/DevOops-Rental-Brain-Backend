package com.devoops.rentalbrain.common.segmentrebuild.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_risk_transition_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTransitionCommandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "from_segment_id", nullable = false)
    private Long fromSegmentId;

    @Column(name = "to_segment_id", nullable = false)
    private Long toSegmentId;

    @Column(name = "reason_code")
    private String reasonCode;

    @Column(name = "reason")
    private String reason;

    @Column(name = "trigger_type", nullable = false)
    private String triggerType; // 'AUTO' 등

    @Column(name = "reference_type")
    private String referenceType; // 'PAY_OVERDUE' | 'ITEM_OVERDUE'

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}
