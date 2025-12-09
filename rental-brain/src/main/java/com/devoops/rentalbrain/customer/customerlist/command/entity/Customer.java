package com.devoops.rentalbrain.customer.customerlist.command.entity;

import jakarta.persistence.*;
import lombok.*;
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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @Column(name = "segment_id", nullable = false)
    private Long segmentId;
}