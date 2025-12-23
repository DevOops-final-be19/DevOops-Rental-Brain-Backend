package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import com.devoops.rentalbrain.employee.query.dto.InChargeDTO;

import java.util.List;

public interface CustomersupportQueryCustomersupportService {
    PageResponseDTO<CustomersupportDTO> getSupportList(CustomersupportSearchDTO criteria);
    CustomersupportDTO getSupportDetail(Long id);
    CustomersupportKpiDTO getSupportKpi();
    List<InChargeDTO> getInChargeList();
}