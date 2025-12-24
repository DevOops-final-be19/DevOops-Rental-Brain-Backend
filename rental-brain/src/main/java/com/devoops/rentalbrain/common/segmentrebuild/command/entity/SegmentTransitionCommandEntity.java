package com.devoops.rentalbrain.common.segmentrebuild.command.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_segment_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SegmentTransitionCommandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "previous_segment_id", nullable = false)
    private Long previousSegmentId;

    @Column(name = "current_segment_id", nullable = false)
    private Long currentSegmentId;

    @Column(name = "reason", nullable = false, length = 255)
    private String reason;

    @Column(name = "trigger_type", nullable = false, length = 50)
    private String triggerType;

    @Column(name = "reference_type", nullable = false, length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (changedAt == null) changedAt = now;
        if (createdAt == null) createdAt = now;
    }
}
