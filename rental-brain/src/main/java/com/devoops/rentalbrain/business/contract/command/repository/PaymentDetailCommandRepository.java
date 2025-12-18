package com.devoops.rentalbrain.business.contract.command.repository;

import com.devoops.rentalbrain.business.contract.command.entity.PaymentDetailCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailCommandRepository extends JpaRepository<PaymentDetailCommandEntity,Long> {
}
