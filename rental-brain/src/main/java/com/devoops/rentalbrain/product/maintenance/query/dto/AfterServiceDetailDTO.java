package com.devoops.rentalbrain.product.maintenance.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AfterServiceDetailDTO {

    private Long id;
    private String after_service_code;
    private String customerName;
    private String customerManager;
    private String itemName;
    private String engineer;
    private String type;
    private LocalDateTime dueDate;
    private String status;
    private String contents;

    private LocalDateTime lastInspectDate;
    private String inspectCycleDays;
    private String inspectCycleLabel;
}
