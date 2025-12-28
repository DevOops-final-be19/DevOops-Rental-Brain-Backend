package com.devoops.rentalbrain.business.contract.query.service;

import com.devoops.rentalbrain.business.contract.query.dto.PaymentOverdueCandidateDTO;

import java.util.List;

public interface PaymentDetailsQueryService {
    List<PaymentOverdueCandidateDTO> findOverdueCandidates();
}
