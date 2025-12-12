package com.devoops.rentalbrain.customer.customersupport.query.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomersupportDTO {
    private Long id;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createDate;
    private String action;

    private String customerName;
    private String empName;
    private String categoryName;
    private String channelName;
}
