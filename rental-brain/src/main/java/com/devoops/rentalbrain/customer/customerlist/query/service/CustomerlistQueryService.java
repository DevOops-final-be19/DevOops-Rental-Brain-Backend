package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.Pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.common.CustomerDto;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerlistQueryService {
    PageResponseDTO<CustomerDto> getCustomerListWithPaging(CustomerlistSearchDTO criteria);

    CustomerDto getCustomerDetail(Long id);
}
