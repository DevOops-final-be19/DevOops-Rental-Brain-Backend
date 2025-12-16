package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO; // Import
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;

public interface CustomerlistQueryService {

    // 페이징 목록 조회 (기존 유지)
    PageResponseDTO<CustomerDTO> getCustomerListWithPaging(CustomerlistSearchDTO criteria);

    // 상세 조회 (반환 타입 변경: CustomerDTO -> CustomerDetailResponseDTO)
    CustomerDetailResponseDTO getCustomerDetail(Long id);
}