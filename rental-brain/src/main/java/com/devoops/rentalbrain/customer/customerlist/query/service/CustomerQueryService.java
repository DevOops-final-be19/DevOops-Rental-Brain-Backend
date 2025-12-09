package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.PageResponse;
import com.devoops.rentalbrain.customer.common.CustomerDto;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerSearchDto;
import com.devoops.rentalbrain.customer.customerlist.query.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerQueryService {

    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    public PageResponse<CustomerDto> getCustomerList(String name, String email, int page, int size) {
        // 검색 조건 DTO 생성 및 값 주입 (PageRequest 필드인 page, size도 함께 설정됨)
        CustomerSearchDto searchDto = new CustomerSearchDto();
        searchDto.setName(name);
        searchDto.setEmail(email);
        searchDto.setPage(page);
        searchDto.setSize(size);

        // 데이터 조회
        List<CustomerDto> list = customerMapper.selectCustomerList(searchDto);
        int totalCount = customerMapper.selectCustomerCount(searchDto);

        // 공통 응답 객체 반환 (hasNext 자동 계산)
        return new PageResponse<>(list, totalCount, searchDto);
    }

    @Transactional(readOnly = true)
    public CustomerDto getCustomerDetail(Long id) {
        return customerMapper.selectCustomerById(id);
    }
}