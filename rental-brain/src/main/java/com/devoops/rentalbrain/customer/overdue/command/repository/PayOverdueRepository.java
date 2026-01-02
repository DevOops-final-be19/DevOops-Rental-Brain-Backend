package com.devoops.rentalbrain.customer.overdue.command.repository;

import com.devoops.rentalbrain.customer.overdue.command.entity.PayOverdue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayOverdueRepository extends JpaRepository<PayOverdue, Long> {
    boolean existsByContractIdAndStatus(Long contractId, String status);
}
