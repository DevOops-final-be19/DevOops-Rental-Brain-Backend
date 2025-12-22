package com.devoops.rentalbrain.customer.customerlist.query.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerHistoryDTO {
    private String type;        // 구분 (문의, 견적, 계약, AS 등)
    private String content;     // 내용
    private LocalDateTime date; // 일시
    private String performer;   // 담당자
    private String status;      // 상태
    private String reason;      // 변경 사유
}