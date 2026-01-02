package com.devoops.rentalbrain.product.maintenance.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AfterServiceDTO {

    private Long id;
    private String after_service_code;
    private String customerName;
    private String customerManager;
    private String itemName;
    private String type;
    private LocalDateTime dueDate;
    private String engineer;
    private String status;
}
