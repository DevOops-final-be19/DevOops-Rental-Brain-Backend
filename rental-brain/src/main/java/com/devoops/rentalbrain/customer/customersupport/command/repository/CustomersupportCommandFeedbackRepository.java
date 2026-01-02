package com.devoops.rentalbrain.customer.customersupport.command.repository;

import com.devoops.rentalbrain.customer.customersupport.command.entity.CustomersupportCommandFeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomersupportCommandFeedbackRepository
        extends JpaRepository<CustomersupportCommandFeedbackEntity, Long> {
}