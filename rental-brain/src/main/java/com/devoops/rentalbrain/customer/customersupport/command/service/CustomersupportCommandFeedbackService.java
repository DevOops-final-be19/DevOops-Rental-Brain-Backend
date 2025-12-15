package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.devoops.rentalbrain.customer.customersupport.command.dto.FeedbackDTO;

public interface CustomersupportCommandFeedbackService {
    Long registerFeedback(FeedbackDTO dto);
    void updateFeedback(Long id, FeedbackDTO dto);
    void deleteFeedback(Long id);
}
