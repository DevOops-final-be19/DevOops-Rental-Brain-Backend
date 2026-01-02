package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import com.devoops.rentalbrain.customer.customersupport.query.mapper.CustomersupportQueryCustomersupportMapper;
import com.devoops.rentalbrain.employee.query.dto.InChargeDTO;
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

    @Override
    public CustomersupportKpiDTO getSupportKpi() {
        return mapper.selectSupportKpi();
    }

    @Override
    public List<InChargeDTO> getInChargeList() {
        return mapper.selectInChargeList();
    }
}