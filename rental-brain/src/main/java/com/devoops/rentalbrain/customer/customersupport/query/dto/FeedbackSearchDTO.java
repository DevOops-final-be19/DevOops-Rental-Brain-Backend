package com.devoops.rentalbrain.customer.customersupport.query.dto;

import com.devoops.rentalbrain.common.pagination.Criteria;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeedbackSearchDTO extends Criteria {
    private String title;
    private Integer star;

    public FeedbackSearchDTO(int page, int size) {
        super(page, size);
    }
}