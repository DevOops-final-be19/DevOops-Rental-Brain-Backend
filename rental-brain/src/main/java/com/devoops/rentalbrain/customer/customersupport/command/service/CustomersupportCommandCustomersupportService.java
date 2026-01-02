package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.devoops.rentalbrain.customer.customersupport.command.dto.CustomersupportDTO;

public interface CustomersupportCommandCustomersupportService {
    Long registerSupport(CustomersupportDTO dto);
    void updateSupport(Long id, CustomersupportDTO dto);
    void deleteSupport(Long id);
}
