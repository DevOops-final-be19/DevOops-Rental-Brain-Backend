package com.devoops.rentalbrain.customer.customersupport.command.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomersupportDTO {
    private Long id;
    private String customerSupportCode;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createDate;
    private String action;

    // [중요] 프론트엔드에서 보내는 변수명과 일치해야 함
    private Long customerId; // 프론트: createForm.customerId
    private Long empId;      // 프론트: createForm.empId
    private Long categoryId; // 프론트: createForm.categoryId
    private Long channelId;  // 프론트: createForm.channelId

    // 조회용 필드 (저장 시엔 안 쓰임)
    private String customerName;
    private String empName;
    private String categoryName;
    private String channelName;
}