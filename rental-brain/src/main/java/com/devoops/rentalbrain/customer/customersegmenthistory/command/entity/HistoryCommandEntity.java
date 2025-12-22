package com.devoops.rentalbrain.customer.customersegmenthistory.command.entity;

import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeReferenceType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeTriggerType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_segment_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class HistoryCommandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "previous_segment_id")
    private Long previousSegmentId;

    @Column(name = "current_segment_id")
    private Long currentSegmentId;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", nullable = false)
    private SegmentChangeTriggerType triggerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private SegmentChangeReferenceType referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    //update 염두
    @PrePersist
    public void prePersist() {
        this.changedAt = LocalDateTime.now();
    }
}
