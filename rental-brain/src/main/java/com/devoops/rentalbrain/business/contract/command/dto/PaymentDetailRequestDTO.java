package com.devoops.rentalbrain.business.contract.command.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDetailRequestDTO {
    private LocalDateTime paymentActual;
}
