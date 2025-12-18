package com.devoops.rentalbrain.customer.customersegmenthistory.command.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class HistoryCommandResponseDTO {

    private Long id;
    private LocalDateTime changedAt;
}
