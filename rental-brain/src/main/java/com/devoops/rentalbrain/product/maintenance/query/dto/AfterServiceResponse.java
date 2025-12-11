package com.devoops.rentalbrain.product.maintenance.query.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AfterServiceResponse {
    private Long id;
    private String engineer;
    private String type;
    private LocalDateTime dueDate;
    private String status;
    private String contents;
    private String customerName;
    private String itemName;
}