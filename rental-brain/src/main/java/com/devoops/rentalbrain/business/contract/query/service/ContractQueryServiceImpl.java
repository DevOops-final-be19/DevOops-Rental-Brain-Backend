package com.devoops.rentalbrain.business.contract.query.service;

import com.devoops.rentalbrain.business.contract.query.dto.AllContractDTO;
import com.devoops.rentalbrain.business.contract.query.dto.ContractSearchDTO;
import com.devoops.rentalbrain.business.contract.query.mapper.ContractQueryMapper;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractQueryServiceImpl implements ContractQueryService {

    private final ContractQueryMapper contractQueryMapper;

    @Autowired
    public ContractQueryServiceImpl(ContractQueryMapper contractQueryMapper) {
        this.contractQueryMapper = contractQueryMapper;
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
}
