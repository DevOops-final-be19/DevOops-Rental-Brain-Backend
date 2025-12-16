package com.devoops.rentalbrain.product.maintenance.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AfterServiceUpdateRequest {

    private String engineer;
    private LocalDateTime dueDate;
    private String status;
    private String contents;
}
