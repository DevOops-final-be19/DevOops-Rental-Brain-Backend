package com.devoops.rentalbrain.customer.customersupport.query.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackDTO {
    private Long id;
    private String feedbackCode;
    private String title;
    private String content;
    private Integer star;
    private LocalDateTime createDate;
    private String action;

    private String customerName;
    private String categoryName;
    private String empName;
    private String channelName;
}