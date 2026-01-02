package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractPaymentDTO {
    private long id;
    private LocalDateTime paymentDue;
    private LocalDateTime paymentActual;
    private Integer overdueDays;
    private String paymentStatus;
    private long conId;
}
