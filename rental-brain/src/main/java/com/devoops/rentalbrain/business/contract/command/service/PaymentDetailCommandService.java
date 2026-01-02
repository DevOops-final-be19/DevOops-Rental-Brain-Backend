package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.dto.PaymentDetailRequestDTO;

public interface PaymentDetailCommandService {
    void completePayment(Long paymentDetailId, PaymentDetailRequestDTO dto);
    void autoMarkAsNonPayment();
    void increaseOverdueDays();
}
