package com.devoops.rentalbrain.approval.command.Repository;

import com.devoops.rentalbrain.approval.command.entity.ApprovalCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ApprovalCommandRepository extends JpaRepository<ApprovalCommandEntity, Long> {
    @Modifying
    @Query("""
    UPDATE ApprovalCommandEntity a
       SET a.contract.id=NULL
     WHERE a.contract.id= :contractId
""")
    void detachApproval(Long contractId);

}
