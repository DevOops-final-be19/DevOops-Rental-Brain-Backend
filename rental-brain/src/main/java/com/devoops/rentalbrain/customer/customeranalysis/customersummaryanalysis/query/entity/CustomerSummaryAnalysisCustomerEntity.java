package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@DynamicInsert
public class CustomerSummaryAnalysisCustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // [추가] 고객 코드
    @Column(name = "customer_code", nullable = false, unique = true, length = 255)
    private String customerCode;

    @Column(nullable = false)
    private String name;

    @Column(name = "in_charge", nullable = false)
    private String inCharge;

    @Column
    private String dept;

    @Column(name = "call_num", nullable = false)
    private String callNum;

    @Column
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(name = "business_num", nullable = false)
    private String businessNum;

    @Column(nullable = false)
    private String addr;

    @Column(name = "last_transaction")
    private LocalDateTime lastTransaction;

    @Column(name = "first_contract_date")
    private LocalDateTime firstContractDate;

    @Column(length = 2000)
    private String memo;

    @Column
    private Integer star;

    @Column(name = "is_deleted", length = 1)
    private String isDeleted;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @Column(name = "segment_id", nullable = false)
    private Long segmentId;
}