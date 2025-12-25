package com.devoops.rentalbrain.dashboard.query.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuarterlyCustomerTrendResponseDTO {
    private int year;
    private List<QuarterlyCustomerTrendItemDTO> series;
}