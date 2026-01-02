package com.devoops.rentalbrain.customer.customersupport.query.dto;

import com.devoops.rentalbrain.common.pagination.Criteria;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomersupportSearchDTO extends Criteria {
    private String keyword;   // 통합 검색 (제목, 내용, 기업명)
    private String status;    // 진행 상태 (P, C 등)
    private Integer category; // 카테고리 ID
    private String sortBy;    // 정렬 기준 컬럼
    private String sortOrder; // 정렬 순서 (ASC/DESC)

    public CustomersupportSearchDTO(int page, int size) {
        super(page, size);
    }
}