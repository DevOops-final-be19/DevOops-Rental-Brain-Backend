package com.devoops.rentalbrain.customer.customersegmenthistory.query.service;

import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.RiskTransitionCountDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.RiskTransitionHistoryDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.mapper.RiskTransitionQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RiskTransitionQueryServiceImpl implements RiskTransitionQueryService {

    private final RiskTransitionQueryMapper riskTransitionQueryMapper;

    @Autowired
    public RiskTransitionQueryServiceImpl(RiskTransitionQueryMapper riskTransitionQueryMapper) {
        this.riskTransitionQueryMapper = riskTransitionQueryMapper;
    }

    @Override
    public List<RiskTransitionHistoryDTO> getChurnRiskTransitions(Long customerId) {

        return riskTransitionQueryMapper.findChurnRiskTransitions(customerId);
    }


    @Override
    public List<RiskTransitionHistoryDTO> getBlacklistTransitions(Long customerId) {

        return riskTransitionQueryMapper.findBlacklistTransitions(customerId);
    }

    // 알림 필요한 부분, 블랙리스트 3회 찍히면 알림 가게
    @Override
    public RiskTransitionCountDTO getRiskTransitionCount(Long customerId) {
        int churnCount = riskTransitionQueryMapper.countChurnRiskTransitions(customerId);
        int blacklistCount = riskTransitionQueryMapper.countBlacklistTransitions(customerId);
        String customerName = riskTransitionQueryMapper.findCustomerName(customerId);

        return new RiskTransitionCountDTO(customerId, customerName, churnCount, blacklistCount);
    }

}
