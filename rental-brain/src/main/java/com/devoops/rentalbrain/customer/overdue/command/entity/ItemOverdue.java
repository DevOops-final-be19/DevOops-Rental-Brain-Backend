package com.devoops.rentalbrain.customer.overdue.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_overdue")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemOverdue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_overdue_code", nullable = false, unique = true)
    private String itemOverdueCode;

    @Column(nullable = false)
    private Integer count;

    // 계약 만료일 (= 제품 회수 기준일)
    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "overdue_period", nullable = false)
    private Integer overduePeriod;

    @Column
    private String status; // P: 미해결, C: 해결

    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "cum_id", nullable = false)
    private Long customerId;

    public void changeCount(Integer count) {
        this.count = count;
    }

    public void resolve() {
        this.status = "C";
    }

    public static ItemOverdue create(
            Long contractId,
            Long customerId,
            Integer count,
            LocalDateTime dueDate,
            Integer overduePeriod
    ) {
        ItemOverdue entity = new ItemOverdue();
        entity.contractId = contractId;
        entity.customerId = customerId;
        entity.count = count;
        entity.dueDate = dueDate;
        entity.overduePeriod = overduePeriod;
        entity.status = "P";
        return entity;
    }
}

