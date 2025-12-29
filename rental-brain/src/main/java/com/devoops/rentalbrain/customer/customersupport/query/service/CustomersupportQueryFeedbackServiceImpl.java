package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackSearchDTO;
import com.devoops.rentalbrain.customer.customersupport.query.mapper.CustomersupportQueryFeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomersupportQueryFeedbackServiceImpl implements CustomersupportQueryFeedbackService {

    private final CustomersupportQueryFeedbackMapper feedbackMapper;

    @Override
    public PageResponseDTO<FeedbackDTO> getFeedbackList(FeedbackSearchDTO searchDTO) {
        // 1. 전체 개수 조회
        int totalCount = feedbackMapper.countFeedbackList(searchDTO);

        // 2. 페이징 정보 계산 (static 메서드 사용)
        PagingButtonInfo pageInfo = Pagination.getPagingButtonInfo(searchDTO, totalCount);

        // 3. 목록 조회 (searchDTO 안에 offset 정보가 자동으로 계산되어 있음)
        List<FeedbackDTO> list = feedbackMapper.selectFeedbackList(searchDTO);

        // 4. 결과 반환
        return new PageResponseDTO<>(list, totalCount, pageInfo);
    }

    @Override
    public FeedbackDTO getFeedbackDetail(Long id) {
        return feedbackMapper.selectFeedbackDetail(id);
    }

    @Override
    public Map<String, Object> getFeedbackKpi() {
        return feedbackMapper.selectFeedbackKpi();
    }
}