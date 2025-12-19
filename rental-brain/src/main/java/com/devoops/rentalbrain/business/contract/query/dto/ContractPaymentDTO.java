package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractPaymentDTO {
    private long id;
    private LocalDateTime payment_due;
    private LocalDateTime payment_actual;
    private Integer overdue_days;
    private String payment_status;
    private long con_id;
}
