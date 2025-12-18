package com.devoops.rentalbrain.customer.customerlist.command.service;

import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;

public interface CustomerlistCommandService {
    // 등록
    Long registerCustomer(CustomerlistCommandDTO dto);

    // 수정 (ModelMapper 사용)
    void updateCustomer(Long id, CustomerlistCommandDTO dto);

    // 삭제 (Soft Delete)
    void deleteCustomer(Long id);

    // [복구] Soft Delete 해제 (Y -> N)
    void restoreCustomer(Long id);
}
