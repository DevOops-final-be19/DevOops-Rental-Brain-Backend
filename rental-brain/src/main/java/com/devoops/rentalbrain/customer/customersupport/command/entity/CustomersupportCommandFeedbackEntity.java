package com.devoops.rentalbrain.customer.customersupport.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
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
@Builder
public class CustomersupportCommandFeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feedback_code", nullable = false, unique = true)
    private String feedbackCode;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer star;

    private String action;

    @Column(name = "create_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "cum_id")
    private Long cumId;

    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "channel_id")
    private Long channelId;

    // 수정 메서드
    public void updateFeedback(String title, String content, Integer star, String action, Long empId, Long categoryId, Long channelId) {
        this.title = title;
        this.content = content;
        this.star = star;
        this.action = action;
        this.empId = empId;
        this.categoryId = categoryId;
        this.channelId = channelId;
    }
}