package com.devoops.rentalbrain.customer.overdue.command.repository;

import com.devoops.rentalbrain.customer.overdue.command.entity.ItemOverdue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemOverdueRepository extends JpaRepository<ItemOverdue, Long> {
    Optional<ItemOverdue> findByContractIdAndStatus(Long contractId, String status);
    boolean existsByContractIdAndStatus(Long contractId, String status);
}
