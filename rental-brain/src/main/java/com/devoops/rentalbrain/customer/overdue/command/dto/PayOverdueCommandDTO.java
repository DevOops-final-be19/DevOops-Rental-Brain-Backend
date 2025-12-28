package com.devoops.rentalbrain.customer.overdue.command.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PayOverdueCommandDTO {

    private LocalDate paidDate;
}
