package com.devoops.rentalbrain.customer.customersupport.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@DynamicInsert
public class CustomersupportCommandFeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    private Integer star;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    private String action;

    @Column(name = "cum_id", nullable = false)
    private Long cumId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "emp_id", nullable = false)
    private Long empId;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @PrePersist
    public void prePersist() {
        if (this.createDate == null) this.createDate = LocalDateTime.now();
    }
}