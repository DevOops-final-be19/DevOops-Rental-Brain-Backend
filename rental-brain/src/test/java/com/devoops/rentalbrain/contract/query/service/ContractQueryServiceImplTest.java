package com.devoops.rentalbrain.contract.query.service;

import com.devoops.rentalbrain.business.contract.query.dto.*;
import com.devoops.rentalbrain.business.contract.query.mapper.ContractApprovalMapper;
import com.devoops.rentalbrain.business.contract.query.mapper.ContractDetailQueryMapper;
import com.devoops.rentalbrain.business.contract.query.mapper.ContractQueryMapper;
import com.devoops.rentalbrain.business.contract.query.service.ContractQueryServiceImpl;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContractQueryServiceImplTest {
    @Mock
    private ContractQueryMapper contractQueryMapper;

    @Mock
    private ContractDetailQueryMapper contractDetailQueryMapper;

    @Mock
    private ContractApprovalMapper contractApprovalMapper;

    @InjectMocks
    private ContractQueryServiceImpl contractQueryService;

    @Test
    @DisplayName("계약 목록 페이징 조회 성공")
    void getContractListWithPaging_success() {
        // given
        ContractSearchDTO criteria = new ContractSearchDTO(1,10);
        criteria.setConName("렌탈 계약");
        criteria.setStatus("P");

        List<AllContractDTO> contracts =
                List.of(new AllContractDTO(), new AllContractDTO());

        when(contractQueryMapper.getContractList(criteria))
                .thenReturn(contracts);
        when(contractQueryMapper.getCountContract(criteria))
                .thenReturn(2L);

        // when
        PageResponseDTO<AllContractDTO> response =
                contractQueryService.getContractListWithPaging(criteria);

        // then
        assertNotNull(response);
        assertEquals(2, response.getContents().size());
        assertEquals(2L, response.getTotalCount());
        assertNotNull(response.getPaging());

        verify(contractQueryMapper).getContractList(criteria);
        verify(contractQueryMapper).getCountContract(criteria);
    }

    @Test
    @DisplayName("계약 요약 정보 조회 성공")
    void getContractSummary_success() {
        // given
        when(contractQueryMapper.countAllContracts()).thenReturn(100L);
        when(contractQueryMapper.countProgressContracts()).thenReturn(60L);
        when(contractQueryMapper.countImminentExpireContracts()).thenReturn(10L);
        when(contractQueryMapper.countThisMonthContracts()).thenReturn(15L);

        // when
        ContractSummaryDTO summary =
                contractQueryService.getContractSummary();

        // then
        assertEquals(100L, summary.getTotalContracts());
        assertEquals(60L, summary.getProgressContracts());
        assertEquals(10L, summary.getImminentExpireContracts());
        assertEquals(15L, summary.getThisMonthContracts());
    }

    @Test
    @DisplayName("계약 기본 정보 조회 성공")
    void getContractBasicInfo_success() {
        // given
        Long contractId = 1L;

        when(contractDetailQueryMapper.selectContractOverview(contractId))
                .thenReturn(new ContractOverviewDTO());
        when(contractDetailQueryMapper.selectContractProgress(contractId))
                .thenReturn(new ContractProgressDTO());
        when(contractDetailQueryMapper.countOverduePayments(contractId))
                .thenReturn(2);
        when(contractDetailQueryMapper.countContractProducts(contractId))
                .thenReturn(5);

        // when
        ContractBasicInfoDTO dto =
                contractQueryService.getContractBasicInfo(contractId);

        // then
        assertNotNull(dto);
        assertEquals(2, dto.getOverdueCount());
        assertEquals(5, dto.getProductCount());
        assertNotNull(dto.getOverview());
        assertNotNull(dto.getProgress());
    }

    @Test
    @DisplayName("계약 상품 정보 조회 성공")
    void getContractItemInfo_success() {
        // given
        Long contractId = 1L;

        ContractItemSummaryDTO summary1 = new ContractItemSummaryDTO();
        summary1.setItemName("노트북");
        summary1.setQuantity(3);

        ContractItemSummaryDTO summary2 = new ContractItemSummaryDTO();
        summary2.setItemName("모니터");
        summary2.setQuantity(2);

        ContractItemDetailDTO detail = new ContractItemDetailDTO();
        // detail 필드 세팅 필요 시 추가

        when(contractDetailQueryMapper.selectContractItemSummary(contractId))
                .thenReturn(List.of(summary1, summary2));

        when(contractDetailQueryMapper.selectContractItemDetails(contractId))
                .thenReturn(List.of(detail));

        // when
        ContractItemInfoDTO dto =
                contractQueryService.getContractItemInfo(contractId);

        // then
        assertNotNull(dto);

        assertEquals(2, dto.getContractItemSummary().size());
        assertEquals("노트북", dto.getContractItemSummary().get(0).getItemName());
        assertEquals(3, dto.getContractItemSummary().get(0).getQuantity());

        assertEquals(1, dto.getContractItemDetail().size());
    }



    @Test
    @DisplayName("계약 결제 내역 조회 성공")
    void getContractPayments_success() {
        // given
        Long contractId = 1L;

        when(contractDetailQueryMapper.selectContractPaymentDetail(contractId))
                .thenReturn(List.of(new ContractPaymentDTO()));

        // when
        List<ContractPaymentDTO> payments =
                contractQueryService.getContractPayments(contractId);

        // then
        assertEquals(1, payments.size());
    }

    @Test
    @DisplayName("계약별 렌탈 상품 목록 조회 성공")
    void getRentalProductList_success() {
        // given
        Long contractId = 1L;

        when(contractQueryMapper.selectRentalProductList(contractId))
                .thenReturn(List.of(new RentalProductInfoDTO()));

        // when
        List<RentalProductInfoDTO> list =
                contractQueryService.getRentalProductList(contractId);

        // then
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("고객 계약 결재 목록 페이징 조회 성공")
    void getCustomerContractApprovalList_success() {
        // given
        Criteria criteria = new Criteria(1, 10);

        when(contractApprovalMapper.CustomerContractList(criteria))
                .thenReturn(List.of(new CustomerContractApprovalDTO()));
        when(contractApprovalMapper.CustomerContractListCount(criteria))
                .thenReturn(1L);

        // when
        PageResponseDTO<CustomerContractApprovalDTO> response =
                contractQueryService.getCustomerContractApprovalList(criteria);

        // then
        assertEquals(1, response.getContents().size());
        assertEquals(1L, response.getTotalCount());
        assertNotNull(response.getPaging());
    }

    @Test
    @DisplayName("계약 담당자 목록 조회 성공")
    void getContractEmpList_success() {
        // given
        when(contractApprovalMapper.getContractEmpList())
                .thenReturn(List.of(new EmpContractDTO()));

        // when
        List<EmpContractDTO> list =
                contractQueryService.getContractEmpList();

        // then
        assertEquals(1, list.size());
    }
}
