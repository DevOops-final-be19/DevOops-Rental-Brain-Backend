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

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "overdue_period", nullable = false)
    private Integer overduePeriod;

    @Column
    private String status; // P: 미해결, C: 해결

    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @Column(name = "cum_id", nullable = false)
    private Long customerId;

    public void resolve() {
        this.status = "C";
    }

    public static ItemOverdue create(
            Long contractId,
            Long customerId,
            Integer count
    ) {
        ItemOverdue entity = new ItemOverdue();
        entity.contractId = contractId;
        entity.customerId = customerId;
        entity.count = count;
        entity.status = "P";
        return entity;
    }
}

