package com.devoops.rentalbrain.business.contract.command.entity;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;


@Entity
@Table(name = "contract")
@Setter
@Getter
@NoArgsConstructor
@ToString
@DynamicInsert
@DynamicUpdate
public class ContractCommandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 계약 코드 (예: CON-2025-001)
     */
    @Column(name = "contract_code", length = 30, nullable = false, unique = true)
    private String contractCode;

    /**
     * 계약명
     */
    @Column(nullable = false)
    private String name;

    /**
     * 계약 시작일
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /**
     * 계약 기간 (개월)
     */
    @Column(name = "contract_period", nullable = false)
    private Integer contractPeriod;

    /**
     * 계약 상태 (계약진행(P),만료임박(D),계약만료(C),결제대기(W),결제거절(R))
     */
    @Column(length = 1, nullable = false)
    private String status;

    /**
     * 월 납부 금액
     */
    @Column(name = "monthly_payment")
    private Integer monthlyPayment;

    /**
     * 총 계약 금액
     */
    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    /**
     * 결제 방식 (예: 카드, 계좌이체 등)
     */
    @Column(name = "pay_method", length = 1, nullable = false)
    private String payMethod;

    /**
     * 특이사항
     */
    @Column(name = "special_content", length = 2000)
    private String specialContent;

    /**
     * 현재 계약 단계
     */
    @Column(name = "current_step", nullable = false)
    private Integer currentStep;

    /**
     * 고객 ID (연관관계 최소화 – 실무형)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cum_id", nullable = false)
    private CustomerlistCommandEntity customer;

    @Column(name = "is_deleted")
    private String isDeleted;

}
