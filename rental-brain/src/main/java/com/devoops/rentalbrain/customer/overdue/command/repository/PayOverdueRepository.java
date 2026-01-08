package com.devoops.rentalbrain.customer.overdue.command.repository;

import com.devoops.rentalbrain.customer.overdue.command.entity.PayOverdue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PayOverdueRepository extends JpaRepository<PayOverdue, Long> {
    boolean existsByContractIdAndStatus(Long contractId, String status);

    @Modifying
    @Query("""
    UPDATE PayOverdue p
       SET p.contractId=NULL
     WHERE p.contractId=:contractId
""")
    void detachPayOverdue(Long contractId);
}
