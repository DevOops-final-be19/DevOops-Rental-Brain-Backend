package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalProductInfoDTO {
    private Long itemId;               // A.ID
    private String itemName;            // A.name
    private String itemStatus;          // A.STATUS
    private LocalDateTime lastInspectDate; // A.LAST_INSPECT_DATE
    private Long customerId;            // C.cum_id
}
