package com.devoops.rentalbrain.common.segmentrebuild.command.repository;

import com.devoops.rentalbrain.common.segmentrebuild.command.entity.RiskTransitionCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskTransitionCommandRepository extends JpaRepository<RiskTransitionCommandEntity, Long> {
}
