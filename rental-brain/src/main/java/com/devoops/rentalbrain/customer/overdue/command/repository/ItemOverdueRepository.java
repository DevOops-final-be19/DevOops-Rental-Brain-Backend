package com.devoops.rentalbrain.customer.overdue.command.repository;

import com.devoops.rentalbrain.customer.overdue.command.entity.ItemOverdue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemOverdueRepository extends JpaRepository<ItemOverdue, Long> {
    Optional<ItemOverdue> findByContractIdAndStatus(Long contractId, String status);
    boolean existsByContractIdAndStatus(Long contractId, String status);

    @Modifying
    @Query("""
    UPDATE ItemOverdue i
      SET i.contractId=NULL
    WHERE i.contractId=:contractId
""")
    void detachItemOverdue(Long contractId);
}
