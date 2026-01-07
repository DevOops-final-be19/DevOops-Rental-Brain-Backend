package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.common.error.ErrorCode;
import com.devoops.rentalbrain.common.error.exception.BusinessException;
import com.devoops.rentalbrain.customer.common.CustomerDTO; // CustomerDTO 사용
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerKpiDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.devoops.rentalbrain.customer.customerlist.query.mapper.CustomerlistQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerlistQueryServiceImpl implements CustomerlistQueryService {

    private final CustomerlistQueryMapper mapper;

    // 1. KPI 조회
    @Override
    public CustomerKpiDTO getCustomerKpi() {
        return mapper.selectCustomerKpi();
    }

    // 2. 페이징 목록 조회
    @Override
    public PageResponseDTO<CustomerDTO> getCustomerListWithPaging(CustomerlistSearchDTO criteria) {
        // 전체 개수 조회
        int totalCount = (int) mapper.selectCustomerCount(criteria);

        // 페이징 정보 생성
        PagingButtonInfo pagingInfo = Pagination.getPagingButtonInfo(criteria, totalCount);

        // 목록 데이터 조회
        List<CustomerDTO> customerList = mapper.selectCustomerList(criteria);

        // ★ 수정: Builder 대신 생성자 사용 (PageResponseDTO에 @Builder가 없음)
        // 생성자 순서: (List<T> contents, long totalCount, PagingButtonInfo paging)
        return new PageResponseDTO<>(customerList, totalCount, pagingInfo);
    }

    // 3. 상세 조회
    @Override
    public CustomerDetailResponseDTO getCustomerDetail(Long id) {
        // 1. 기본 정보 조회
        CustomerDetailResponseDTO detail = mapper.selectCustomerDetail(id);
        if (detail == null) {
            throw new RuntimeException("해당 고객을 찾을 수 없습니다.");
        }

        // 2. [종합 정보] 탭용 통합 히스토리
        // 파라미터: (고객ID, offset, amount)
        detail.setHistoryList(mapper.selectCustomerHistory(id, 0, Integer.MAX_VALUE));

        // 3. 탭별 데이터 조회 및 설정
        detail.setSupportList(mapper.selectSupportList(id));
        detail.setQuoteList(mapper.selectQuoteList(id));
        detail.setContractList(mapper.selectContractList(id));
        detail.setAsList(mapper.selectAsList(id));
        detail.setFeedbackList(mapper.selectFeedbackList(id));
        detail.setCouponList(mapper.selectCouponList(id));
        detail.setPromotionList(mapper.selectPromotionList(id));
        detail.setSegmentHistoryList(mapper.selectSegmentHistory(id));

        return detail;
    }

}