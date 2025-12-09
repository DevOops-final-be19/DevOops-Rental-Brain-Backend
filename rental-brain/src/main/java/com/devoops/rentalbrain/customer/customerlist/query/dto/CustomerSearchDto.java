package com.devoops.rentalbrain.customer.customerlist.query.dto;

import com.devoops.rentalbrain.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)    // 
public class CustomerSearchDto extends PageRequest {
    private String name;
    private String email;
}