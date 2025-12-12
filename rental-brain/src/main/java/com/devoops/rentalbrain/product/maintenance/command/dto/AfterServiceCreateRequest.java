package com.devoops.rentalbrain.product.maintenance.command.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AfterServiceCreateRequest {
    private String engineer;
    private String type;             // R or A
    private LocalDateTime dueDate;
    private String contents;
    private Long itemId;
    private Long customerId;
}
