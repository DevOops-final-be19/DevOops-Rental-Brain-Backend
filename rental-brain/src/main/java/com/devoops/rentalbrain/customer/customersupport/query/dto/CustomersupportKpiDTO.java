package com.devoops.rentalbrain.customer.customersupport.query.dto;

import lombok.Data;

@Data
public class CustomersupportKpiDTO {
    private long total;            // 전체 건수
    private long processing;       // 처리 중 건수
    private double resolutionRate; // 해결율 (%)
}