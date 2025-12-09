package com.devoops.rentalbrain.customer.customerlist.command.repository;

import com.devoops.rentalbrain.customer.customerlist.command.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}