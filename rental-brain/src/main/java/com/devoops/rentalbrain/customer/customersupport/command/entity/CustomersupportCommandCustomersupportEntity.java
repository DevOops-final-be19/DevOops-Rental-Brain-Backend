package com.devoops.rentalbrain.customer.customersupport.command.entity;

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
public class CustomersupportCommandCustomersupportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_support_code")
    private String customerSupportCode;

    private String title;

    private String content;

    // [중요] 누락되었던 필드 추가
    @Column(name = "status")
    private String status; // 'P': 진행중, 'C': 완료 등

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "action")
    private String action;

    // [핵심] 고객 ID 매핑 (DB 컬럼: cum_id)
    @Column(name = "cum_id")
    private Long cumId;

    // [핵심] 담당자 ID 매핑 (DB 컬럼: emp_id)
    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "channel_id")
    private Long channelId;

    @PrePersist
    public void prePersist() {
        if (this.createDate == null) this.createDate = LocalDateTime.now();
        // status는 null일 경우 @DynamicInsert에 의해 DB Default('P')가 들어감
    }
}