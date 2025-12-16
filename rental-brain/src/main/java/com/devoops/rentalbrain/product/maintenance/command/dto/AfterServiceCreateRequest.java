package com.devoops.rentalbrain.product.maintenance.command.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AfterServiceCreateRequest {

//    private Long id;

//    채번 생성기
//    private String afterServiceCode;

    private String engineer;
    private String type;          // A / R
    private LocalDateTime dueDate;
    private String contents;
    private Long itemId;
    private Long customerId;
}
