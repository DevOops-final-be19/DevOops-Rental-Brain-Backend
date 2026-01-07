package com.devoops.rentalbrain.approval.command.Repository;

import com.devoops.rentalbrain.approval.command.entity.ApprovalCommandEntity;
import com.devoops.rentalbrain.approval.command.entity.ApprovalMappingCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApprovalMappingCommandRepository extends JpaRepository<ApprovalMappingCommandEntity, Long> {
    List<ApprovalMappingCommandEntity> findByApprovalId(Long approvalId);
    boolean existsByApproval_IdAndIsApprovedNot(Long approvalId, String isApproved);
    boolean existsByApproval_IdAndStepLessThanAndIsApprovedNot(
            Long approvalId,
            Integer step,
            String isApproved
    );

    ApprovalMappingCommandEntity findByApprovalIdAndIsApproved(Long approvalId, String isApproved);

    @Modifying
    @Query("""
    DELETE FROM ApprovalMappingCommandEntity m
    WHERE m.approval.contract.id = :contractId
""")
    void deleteAllByContractId(Long contractId);
}
