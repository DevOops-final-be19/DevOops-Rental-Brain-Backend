package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import com.devoops.rentalbrain.customer.customersupport.query.mapper.CustomersupportQueryFeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomersupportQueryFeedbackServiceImpl implements CustomersupportQueryFeedbackService {

    private final CustomersupportQueryFeedbackMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<FeedbackDTO> getFeedbackList(FeedbackSearchDTO criteria) {
        List<FeedbackDTO> list = mapper.selectFeedbackList(criteria);
        long totalCount = mapper.selectFeedbackCount(criteria);
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(criteria, totalCount);

        return new PageResponseDTO<>(list, totalCount, paging);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackDTO getFeedbackDetail(Long id) {
        return mapper.selectFeedbackById(id);
    }
}