package com.devoops.rentalbrain.business.contract.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_details")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailCommandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 결제 예정일
     */
    @Column(name = "payment_due", nullable = false)
    private LocalDateTime paymentDue;

    /**
     * 실제 결제일
     */
    @Column(name = "payment_actual")
    private LocalDateTime paymentActual;

    /**
     * 연체 일수
     */
    @Column(name = "overdue_days")
    private Integer overdueDays;

    /**
     * 결제 상태
     * P : Pending(예정)
     * C : Complete(완료)
     * N : NonPayment(미납)
     */
    @Column(name = "payment_status", length = 1, nullable = false)
    private String paymentStatus;

    /**
     * 계약 ID (FK)
     */
    @Column(name = "con_id")
    private Long contractId;


}
