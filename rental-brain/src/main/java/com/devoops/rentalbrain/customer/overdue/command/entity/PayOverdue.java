package com.devoops.rentalbrain.customer.overdue.command.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pay_overdue")
@Getter
@NoArgsConstructor
public class PayOverdue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_overdue_code", nullable = false, unique = true)
    private String payOverdueCode;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "overdue_period", nullable = false)
    private Integer overduePeriod;

    @Column(name = "status")
    private String status; // P / C

    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "cum_id", nullable = false)
    private Long customerId;

    public void resolve(LocalDateTime paidDate) {
        this.paidDate = paidDate;
        this.status = "C";
    }

    public void changeStatus(String status) {
        this.status = status;
    }

    public static PayOverdue create(
            Long contractId,
            Long customerId,
            LocalDateTime dueDate,
            Integer overduePeriod
    ) {
        PayOverdue entity = new PayOverdue();
        entity.contractId = contractId;
        entity.customerId = customerId;
        entity.dueDate = dueDate;
        entity.overduePeriod = overduePeriod;
        entity.status = "P";
        return entity;
    }

    public void setPayOverdueCode(String payOverdueCode) {
        this.payOverdueCode = payOverdueCode;
    }

}
