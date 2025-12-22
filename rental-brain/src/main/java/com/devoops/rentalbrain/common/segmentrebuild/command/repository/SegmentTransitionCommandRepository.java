package com.devoops.rentalbrain.common.segmentrebuild.command.repository;

import com.devoops.rentalbrain.common.segmentrebuild.command.mapper.SegmentTransitionCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentTransitionCommandRepository extends JpaRepository<SegmentTransitionCommandEntity, Long> {
}
