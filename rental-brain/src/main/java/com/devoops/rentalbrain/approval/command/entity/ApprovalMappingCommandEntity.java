package com.devoops.rentalbrain.approval.command.entity;

import com.devoops.rentalbrain.employee.command.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "approval_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalMappingCommandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 승인자 사원
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    /**
     * 승인 문서
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id", nullable = false)
    private ApprovalCommandEntity approval;

    /**
     * 승인 단계
     * 1: 팀원
     * 2: 팀장
     * 3: 대표
     */
    @Column
    private Integer step;

    /**
     * 승인 여부
     * Y : 승인
     * N : 반려
     * U: 대기
     */
    @Column(name = "is_approved", length = 1)
    private String isApproved;

    /**
     * 반려 사유
     */
    @Column(name = "reject_reason", length = 255)
    private String rejectReason;
}
