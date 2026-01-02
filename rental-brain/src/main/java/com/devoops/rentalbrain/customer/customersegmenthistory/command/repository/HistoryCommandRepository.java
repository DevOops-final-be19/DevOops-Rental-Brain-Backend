package com.devoops.rentalbrain.customer.customersegmenthistory.command.repository;

import com.devoops.rentalbrain.customer.customersegmenthistory.command.entity.HistoryCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryCommandRepository extends JpaRepository<HistoryCommandEntity, Long> {
}
