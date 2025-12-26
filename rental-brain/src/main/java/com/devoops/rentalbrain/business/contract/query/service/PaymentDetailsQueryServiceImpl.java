package com.devoops.rentalbrain.business.contract.query.service;

import com.devoops.rentalbrain.business.contract.query.dto.PaymentOverdueCandidateDTO;
import com.devoops.rentalbrain.business.contract.query.mapper.PaymentDetailsQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("paymentDetailsQueryService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentDetailsQueryServiceImpl implements PaymentDetailsQueryService{

    private final PaymentDetailsQueryMapper mapper;

    @Override
    public List<PaymentOverdueCandidateDTO> findOverdueCandidates() {
        return mapper.findOverdueCandidates();
    }
}
