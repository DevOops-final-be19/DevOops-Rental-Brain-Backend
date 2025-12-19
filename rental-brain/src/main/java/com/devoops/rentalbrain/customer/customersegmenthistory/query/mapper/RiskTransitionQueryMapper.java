package com.devoops.rentalbrain.customer.customersegmenthistory.query.mapper;

import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.RiskTransitionHistoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface RiskTransitionQueryMapper {

    // 이탈 위험(4) 전환 이력
    List<RiskTransitionHistoryDTO> findChurnRiskTransitions(@Param("customerId") Long customerId);

    // 블랙리스트(6) 전환 이력
    List<RiskTransitionHistoryDTO> findBlacklistTransitions(@Param("customerId") Long customerId);

    // 이탈 위험(4) 전환 횟수
    int countChurnRiskTransitions(@Param("customerId") Long customerId);

    // 블랙리스트(6) 전환 횟수
    int countBlacklistTransitions(@Param("customerId") Long customerId);

    // 고객 이름 가져오기
    String findCustomerName(@Param("customerName") Long customerId);
}
