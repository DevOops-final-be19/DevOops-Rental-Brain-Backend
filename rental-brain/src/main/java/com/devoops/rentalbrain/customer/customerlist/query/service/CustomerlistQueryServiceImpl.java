package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.devoops.rentalbrain.customer.customerlist.query.mapper.CustomerlistQueryMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerlistQueryServiceImpl implements CustomerlistQueryService {

    private final CustomerlistQueryMapper customerlistQueryMapper;
    private final ModelMapper modelMapper; // 객체 복사를 위해 사용 (없으면 new DTO() 후 setter 사용)

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CustomerDTO> getCustomerListWithPaging(CustomerlistSearchDTO criteria) {
        List<CustomerDTO> list = customerlistQueryMapper.selectCustomerList(criteria);
        long totalCount = customerlistQueryMapper.selectCustomerCount(criteria);
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(criteria, totalCount);
        return new PageResponseDTO<>(list, totalCount, paging);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDetailResponseDTO getCustomerDetail(Long id) {
        // 1. 기본 고객 정보 조회
        CustomerDTO customer = customerlistQueryMapper.selectCustomerById(id);
        if (customer == null) {
            throw new IllegalArgumentException("해당 고객이 존재하지 않습니다. ID=" + id);
        }

        // 2. 응답 DTO 생성 및 기본 정보 매핑
        CustomerDetailResponseDTO detail = modelMapper.map(customer, CustomerDetailResponseDTO.class);

        // 3. 연관 데이터 조회 (Join 대신 별도 Select 실행)
        detail.setSupportList(customerlistQueryMapper.selectSupportsByCustomerId(id));
        detail.setFeedbackList(customerlistQueryMapper.selectFeedbacksByCustomerId(id));
        detail.setQuoteList(customerlistQueryMapper.selectQuotesByCustomerId(id));
        detail.setContractList(customerlistQueryMapper.selectContractsByCustomerId(id));
        detail.setAsList(customerlistQueryMapper.selectAsByCustomerId(id));
        detail.setCouponList(customerlistQueryMapper.selectCouponsByCustomerId(id));
        detail.setPromotionList(customerlistQueryMapper.selectPromotionsByCustomerId(id));

        return detail;
    }
}