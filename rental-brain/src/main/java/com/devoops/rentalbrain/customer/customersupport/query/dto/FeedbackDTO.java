package com.devoops.rentalbrain.customer.customersupport.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private Long id;
    private String feedbackCode;
    private String title;
    private String content;
    private Integer star;
    private String action;      // 조치사항 (값이 있으면 완료, 없으면 미완료로 간주)
    private String createDate;

    // Join Fields
    private Long cumId;
    private String customerName; // 기업명
    private Long empId;
    private String empName;      // 담당자
    private Long categoryId;
    private String categoryName; // 카테고리
    private Long channelId;
    private String channelName;  // 유입 채널
}