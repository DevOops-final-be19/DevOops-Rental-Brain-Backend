package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.Pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.Pagination.Pagination;
import com.devoops.rentalbrain.common.Pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.common.CustomerDto;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.devoops.rentalbrain.customer.customerlist.query.mapper.CustomerlistQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerlistQueryService {

    private final CustomerlistQueryMapper customerlistQueryMapper;

    @Transactional(readOnly = true)
    public PageResponseDTO<CustomerDto> getCustomerListWithPaging(CustomerlistSearchDTO criteria) {

        // 1. 데이터 조회 (Mapper 호출)
        List<CustomerDto> list = customerlistQueryMapper.selectCustomerList(criteria);

        // 2. 전체 개수 조회
        long totalCount = customerlistQueryMapper.selectCustomerCount(criteria);

        // 3. 페이지 버튼 정보 계산 (공통 유틸 사용)
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(criteria, totalCount);

        // 4. 공통 응답 DTO 반환
        return new PageResponseDTO<>(list, totalCount, paging);
    }

    @Transactional(readOnly = true)
    public CustomerDto getCustomerDetail(Long id) {
        return customerlistQueryMapper.selectCustomerById(id);
    }
}