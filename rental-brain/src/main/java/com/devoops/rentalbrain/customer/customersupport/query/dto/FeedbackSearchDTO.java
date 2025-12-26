package com.devoops.rentalbrain.customer.customersupport.query.dto;

import com.devoops.rentalbrain.common.pagination.Criteria;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeedbackSearchDTO extends Criteria {
    private String status; // 'P' or 'C'
    private Integer category;
    private String sortBy;
    private String sortOrder;

    public FeedbackSearchDTO(int page, int amount) {
        super(page, amount);
    }
}