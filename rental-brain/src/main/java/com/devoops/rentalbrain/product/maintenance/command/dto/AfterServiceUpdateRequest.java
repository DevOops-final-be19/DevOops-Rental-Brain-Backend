package com.devoops.rentalbrain.product.maintenance.command.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AfterServiceUpdateRequest {
    private String engineer;
    private LocalDateTime dueDate;
    private String status;      // 완료 시 C
    private String contents;
}