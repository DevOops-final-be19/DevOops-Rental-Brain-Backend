package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.common.CustomerDto;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;

public interface CustomerlistQueryService {
    PageResponseDTO<CustomerDto> getCustomerListWithPaging(CustomerlistSearchDTO criteria);

    CustomerDto getCustomerDetail(Long id);
}
