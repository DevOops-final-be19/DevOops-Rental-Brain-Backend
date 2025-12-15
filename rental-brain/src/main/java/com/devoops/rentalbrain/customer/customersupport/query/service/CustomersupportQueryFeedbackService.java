package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;

public interface CustomersupportQueryFeedbackService {
    PageResponseDTO<FeedbackDTO> getFeedbackList(FeedbackSearchDTO criteria);
    FeedbackDTO getFeedbackDetail(Long id);
}