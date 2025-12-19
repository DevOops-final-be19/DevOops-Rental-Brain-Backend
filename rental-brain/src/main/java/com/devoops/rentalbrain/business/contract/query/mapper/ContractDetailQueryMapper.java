package com.devoops.rentalbrain.business.contract.query.mapper;

import com.devoops.rentalbrain.business.contract.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractDetailQueryMapper {
    /* 계약 개요 */
    ContractOverviewDTO selectContractOverview(
            @Param("contractId") Long contractId
    );

    /* 계약 수납 연체 건수 */
    int countOverduePayments(
            @Param("contractId") Long contractId
    );

    /* 계약 진행률 */
    ContractProgressDTO selectContractProgress(
            @Param("contractId") Long contractId
    );

    /* 렌탈 자산 개수 */
    int countContractProducts(
            @Param("contractId") Long contractId
    );

    /* 아이템 목록 요약 */
    List<ContractItemSummaryDTO> selectContractItemSummary(
            @Param("contractId") Long contractId
    );

    /* 아이템 상세 */
    List<ContractItemDetailDTO> selectContractItemDetails(
            @Param("contractId") Long contractId
    );

    /* 결제내역 상세*/
    List<ContractPaymentDTO> selectContractPaymentDetail(
            @Param("contractId") Long contractId
    );
}
