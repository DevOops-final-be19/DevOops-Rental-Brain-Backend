package com.devoops.rentalbrain.business.contract.query.mapper;

import com.devoops.rentalbrain.business.contract.query.dto.PaymentOverdueCandidateDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentDetailsQueryMapper {
    List<PaymentOverdueCandidateDTO> findOverdueCandidates();
}
