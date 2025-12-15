package com.devoops.rentalbrain.customer.customersupport.query.dto;

import com.devoops.rentalbrain.common.pagination.Criteria;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomersupportSearchDTO extends Criteria {
    private String title;
    private String status;

    public CustomersupportSearchDTO(int page, int size) {
        super(page, size);
    }
}