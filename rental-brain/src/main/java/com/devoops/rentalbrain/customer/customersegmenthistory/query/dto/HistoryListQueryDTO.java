package com.devoops.rentalbrain.customer.customersegmenthistory.query.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoryListQueryDTO {
    private Long customerId;
    private String customerName;
    private List<HistoryQueryDTO> histories;

}
