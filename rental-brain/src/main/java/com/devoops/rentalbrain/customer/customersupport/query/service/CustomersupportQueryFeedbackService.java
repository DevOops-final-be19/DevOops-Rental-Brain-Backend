package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackSearchDTO;
import java.util.Map;

public interface CustomersupportQueryFeedbackService {
    PageResponseDTO<FeedbackDTO> getFeedbackList(FeedbackSearchDTO searchDTO);
    FeedbackDTO getFeedbackDetail(Long id);
    Map<String, Object> getFeedbackKpi();
}