package com.devoops.rentalbrain.customer.customersupport.query.dto;

import com.devoops.rentalbrain.common.pagination.Criteria;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackSearchDTO extends Criteria {
    private String status; // 'P' or 'C'
    private Integer category;
    private Integer star; // 별점 필터
    private String keyword;
    private String sortBy;
    private String sortOrder;

    public FeedbackSearchDTO(int page, int amount) {
        super(page, amount);
    }
}