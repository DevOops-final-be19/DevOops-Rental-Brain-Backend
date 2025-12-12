package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.common.Pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.Pagination.Pagination;
import com.devoops.rentalbrain.common.Pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import com.devoops.rentalbrain.customer.customersupport.query.mapper.CustomersupportQueryCustomersupportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomersupportQueryCustomersupportServiceImpl implements CustomersupportQueryCustomersupportService {

    private final CustomersupportQueryCustomersupportMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CustomersupportDTO> getSupportList(CustomersupportSearchDTO criteria) {
        List<CustomersupportDTO> list = mapper.selectSupportList(criteria);
        long totalCount = mapper.selectSupportCount(criteria);
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(criteria, totalCount);

        return new PageResponseDTO<>(list, totalCount, paging);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomersupportDTO getSupportDetail(Long id) {
        return mapper.selectSupportById(id);
    }
}