package com.devoops.rentalbrain.customer.customerlist.query.dto;

import com.devoops.rentalbrain.common.pagination.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerlistSearchDTO extends Criteria {
    private String name;
    private String email;

    // 필터 조건을 위한 필드 추가
    private List<String> segments; // 세그먼트 (예: ["VIP 고객", "신규 고객"])
    private String status;         // 상태 (예: "ACTIVE", "INACTIVE", "ALL")

    // [추가] 정렬 관련 필드 추가
    private String sortBy;
    private String sortOrder;

    public CustomerlistSearchDTO(int page, int size) {
        super(page, size);
    }
}