package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "customer_churn_snapshot",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_snapshot_month_customer", columnNames = {"snapshot_month", "customer_id"})
        },
        indexes = {
                @Index(name = "idx_snapshot_month", columnList = "snapshot_month"),
                @Index(name = "idx_snapshot_risk", columnList = "snapshot_month,is_churn_risk"),
                @Index(name = "idx_snapshot_reason", columnList = "snapshot_month,risk_reason_code")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSummaryAnalysisCustomerChurnSnapshotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "snapshot_month", nullable = false, length = 7)
    private String snapshotMonth; // YYYY-MM

    @Column(name = "customer_id", nullable = false)
    private Long customerId;    // 고객ID

    @Column(name = "is_churn_risk", nullable = false, length = 1)
    private String isChurnRisk; // 이탈위험여부(Y/N) - N이면 즉시 복귀

    @Column(name = "risk_reason_code", nullable = false, length = 30)
    private String riskReasonCode; // EXPIRING/LOW_SAT/OVERDUE/NO_RENEWAL/NONE

    @Column(name = "prev_segment_id")
    private Long prevSegmentId;   // 이전세그먼트ID(위험 이동 전 출처)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;





}
