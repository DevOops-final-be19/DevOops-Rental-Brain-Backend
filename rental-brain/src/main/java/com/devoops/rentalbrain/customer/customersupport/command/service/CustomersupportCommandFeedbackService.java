package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;

public interface CustomersupportCommandFeedbackService {
    void createFeedback(FeedbackDTO dto);
    void updateFeedback(Long id, FeedbackDTO dto);
    void deleteFeedback(Long id);
}