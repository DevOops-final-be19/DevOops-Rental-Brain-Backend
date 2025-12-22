package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_support")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@DynamicInsert // DB Default 값('P') 적용을 위해 사용
public class CustomerSummaryAnalysisCustomerSupportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_support_code", nullable = false, unique = true)
    private String customerSupportCode; // [추가]

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    // DB Default 'P' 사용을 위해 nullable = false 제거 (Java에서는 null 허용)
    @Column(length = 1)
    private String status;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    private String action;

    @Column(name = "cum_id", nullable = false)
    private Long cumId;

    @Column(name = "emp_id", nullable = false)
    private Long empId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @PrePersist
    public void prePersist() {
        if (this.createDate == null) this.createDate = LocalDateTime.now();
        // status는 null일 경우 @DynamicInsert에 의해 DB Default('P')가 들어감
    }
}