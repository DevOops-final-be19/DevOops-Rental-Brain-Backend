package com.devoops.rentalbrain.approval.command.Repository;

import com.devoops.rentalbrain.approval.command.entity.ApprovalMappingCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalMappingCommandRepository extends JpaRepository<ApprovalMappingCommandEntity, Long> {
}
