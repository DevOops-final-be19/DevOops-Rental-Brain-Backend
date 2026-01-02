package com.devoops.rentalbrain.approval.command.Repository;

import com.devoops.rentalbrain.approval.command.entity.ApprovalMappingCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalMappingCommandRepository extends JpaRepository<ApprovalMappingCommandEntity, Long> {
    List<ApprovalMappingCommandEntity> findByApprovalId(Long approvalId);
    boolean existsByApproval_IdAndIsApprovedNot(Long approvalId, String isApproved);
    boolean existsByApproval_IdAndStepLessThanAndIsApprovedNot(
            Long approvalId,
            Integer step,
            String isApproved
    );
}
