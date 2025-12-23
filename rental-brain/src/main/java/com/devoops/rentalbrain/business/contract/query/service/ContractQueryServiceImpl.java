package com.devoops.rentalbrain.business.contract.query.service;

import com.devoops.rentalbrain.business.contract.query.dto.*;
import com.devoops.rentalbrain.business.contract.query.mapper.ContractApprovalMapper;
import com.devoops.rentalbrain.business.contract.query.mapper.ContractDetailQueryMapper;
import com.devoops.rentalbrain.business.contract.query.mapper.ContractQueryMapper;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractQueryServiceImpl implements ContractQueryService {

    private final ContractQueryMapper contractQueryMapper;
    private final ContractDetailQueryMapper contractDetailQueryMapper;
    private final ContractApprovalMapper contractApprovalMapper;

    @Autowired
    public ContractQueryServiceImpl(ContractQueryMapper contractQueryMapper,
                                    ContractDetailQueryMapper contractDetailQueryMapper,
                                    ContractApprovalMapper contractApprovalMapper) {
        this.contractQueryMapper = contractQueryMapper;
        this.contractDetailQueryMapper = contractDetailQueryMapper;
        this.contractApprovalMapper = contractApprovalMapper;
    }

    @Override
    public PageResponseDTO<AllContractDTO> getContractListWithPaging(ContractSearchDTO criteria) {

        // 데이터 조회
        List<AllContractDTO> list = contractQueryMapper.getContractList(criteria);

        // 전체 개수 조회
        long totalCount = contractQueryMapper.getCountContract(criteria);

        // 페이지 버튼 정보계산
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(criteria, totalCount);

        return new PageResponseDTO<>(list, totalCount, paging);
    }

    @Override
    public ContractSummaryDTO getContractSummary() {
        long totalContracts =
                contractQueryMapper.countAllContracts();

        long progressContracts =
                contractQueryMapper.countProgressContracts();

        long imminentExpireContracts =
                contractQueryMapper.countImminentExpireContracts();

        long thisMonthContracts =
                contractQueryMapper.countThisMonthContracts();

        return new ContractSummaryDTO(
                totalContracts,
                progressContracts,
                imminentExpireContracts,
                thisMonthContracts
        );
    }

    @Override
    public ContractBasicInfoDTO getContractBasicInfo(Long contractId) {
        ContractBasicInfoDTO dto = new ContractBasicInfoDTO();
        dto.setOverview(contractDetailQueryMapper.selectContractOverview(contractId));
        dto.setProgress(contractDetailQueryMapper.selectContractProgress(contractId));
        dto.setOverdueCount(contractDetailQueryMapper.countOverduePayments(contractId));
        dto.setProductCount(contractDetailQueryMapper.countContractProducts(contractId));

        return dto;
    }

    @Override
    public ContractItemInfoDTO getContractItemInfo(Long contractId) {
        ContractItemInfoDTO dto = new ContractItemInfoDTO();

        dto.setContractItemSummary(contractDetailQueryMapper.selectContractItemSummary(contractId));
        dto.setContractItemDetail(contractDetailQueryMapper.selectContractItemDetails(contractId));

        return dto;
    }

    @Override
    public List<ContractPaymentDTO> getContractPayments(Long contractId) {
        return contractDetailQueryMapper.selectContractPaymentDetail(contractId);
    }

    @Override
    public List<RentalProductInfoDTO> getRentalProductList(Long contractId) {
        return contractQueryMapper.selectRentalProductList(contractId);
    }

    @Override
    public PageResponseDTO<CustomerContractApprovalDTO> getCustomerContractApprovalList(Criteria criteria) {
        List<CustomerContractApprovalDTO> list =
                contractApprovalMapper.CustomerContractList(criteria);

        long total =
                contractApprovalMapper.CustomerContractListCount(criteria);

        // 3. 페이지 버튼 정보 생성
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, total);

        return new PageResponseDTO<>(list, total, paging);
    }

    @Override
    public List<EmpContractDTO> getContractEmpList() {
        return contractApprovalMapper.getContractEmpList();
    }
}


