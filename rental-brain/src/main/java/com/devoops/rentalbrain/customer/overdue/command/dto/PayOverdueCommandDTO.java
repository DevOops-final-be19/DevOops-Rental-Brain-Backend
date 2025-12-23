package com.devoops.rentalbrain.customer.overdue.command.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PayOverdueCommandDTO {

    private LocalDateTime paidDate;
}
