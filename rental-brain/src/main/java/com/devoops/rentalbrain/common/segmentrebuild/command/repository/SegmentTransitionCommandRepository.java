package com.devoops.rentalbrain.common.segmentrebuild.command.repository;

import com.devoops.rentalbrain.common.segmentrebuild.command.entity.SegmentTransitionCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentTransitionCommandRepository extends JpaRepository<SegmentTransitionCommandEntity, Long> {
}
