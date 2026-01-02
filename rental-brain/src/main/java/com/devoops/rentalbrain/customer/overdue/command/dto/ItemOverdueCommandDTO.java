package com.devoops.rentalbrain.customer.overdue.command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOverdueCommandDTO {
    /**
     * 연체 수량 (선택)
     * - null 이면 수량 변경 안 함
     */
    private Integer count;

    /**
     * 회수 완료 여부
     * true → 해결(C)
     */
    private Boolean resolved;
}
