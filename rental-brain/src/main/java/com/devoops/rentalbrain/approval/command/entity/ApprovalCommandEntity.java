package com.devoops.rentalbrain.approval.command.entity;

import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.employee.command.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalCommandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "approval_code", length = 30, nullable = false, unique = true)
    private String approvalCode;   // APP-2025-001

    @Column(nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    /**
     * 상태
     * P : 대기
     * A : 승인
     * R : 반려
     */
    @Column(length = 1, nullable = false)
    private String status;

    /* ===================== 연관관계 ===================== */

    /**
     * 계약 (N:1)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private ContractCommandEntity contract;

    /**
     * 기안자 사원 (N:1)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;
}
