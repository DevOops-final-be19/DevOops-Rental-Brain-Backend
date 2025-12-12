package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.common.Pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;

public interface CustomersupportQueryCustomersupportService {
    PageResponseDTO<CustomersupportDTO> getSupportList(CustomersupportSearchDTO criteria);
    CustomersupportDTO getSupportDetail(Long id);
}