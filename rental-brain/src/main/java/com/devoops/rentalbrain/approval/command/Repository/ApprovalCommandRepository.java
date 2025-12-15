package com.devoops.rentalbrain.approval.command.Repository;

import com.devoops.rentalbrain.approval.command.entity.ApprovalCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalCommandRepository extends JpaRepository<ApprovalCommandEntity, Long> {
}
