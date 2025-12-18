package com.devoops.rentalbrain.customer.customerlist.query.dto;

import lombok.Data;

@Data
public class CustomerKpiDTO {
    private Integer totalCustomers;      // 총 거래 고객
    private Integer vipCustomers;        // VIP 고객
    private Integer riskCustomers;       // 이탈 위험 고객
    private Integer blacklistCustomers;  // 블랙리스트 고객
}