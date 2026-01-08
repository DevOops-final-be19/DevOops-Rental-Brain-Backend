package com.devoops.rentalbrain.business.contract.command.repository;

import com.devoops.rentalbrain.business.contract.command.entity.ContractItemCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ContractItemCommandRepository extends JpaRepository<ContractItemCommandEntity, Long> {

    @Modifying
    @Query("""
    UPDATE ContractItemCommandEntity c
       SET c.contractId=NULL
     WHERE c.contractId=:contractId
""")
    void detachContractItem(Long contractId);
}
