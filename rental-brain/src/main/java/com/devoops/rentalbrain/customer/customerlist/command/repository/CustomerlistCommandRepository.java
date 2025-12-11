package com.devoops.rentalbrain.customer.customerlist.command.repository;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerlistCommandRepository extends JpaRepository<CustomerlistCommandEntity, Long> {
}