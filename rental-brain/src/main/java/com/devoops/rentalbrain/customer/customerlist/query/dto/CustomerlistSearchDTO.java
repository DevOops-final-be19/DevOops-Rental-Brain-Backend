package com.devoops.rentalbrain.customer.customerlist.query.dto;

import com.devoops.rentalbrain.common.pagination.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerlistSearchDTO extends Criteria {
    private String name;
    private String email;

    // 생성자에서 페이지 정보 설정
    public CustomerlistSearchDTO(int page, int size) {
        super(page, size);
    }
}