package com.devoops.rentalbrain.customer.segment.command.repository;

import com.devoops.rentalbrain.customer.segment.command.entity.SegmentCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentCommandRepository extends JpaRepository<SegmentCommandEntity, Long> {
    SegmentCommandEntity findAllBySegmentName(String segmentName);
}
