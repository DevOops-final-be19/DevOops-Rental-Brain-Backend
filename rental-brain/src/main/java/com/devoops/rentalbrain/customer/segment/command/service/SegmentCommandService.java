package com.devoops.rentalbrain.customer.segment.command.service;

import com.devoops.rentalbrain.customer.segment.command.dto.SegmentCommandCreateDTO;
import com.devoops.rentalbrain.customer.segment.command.dto.SegmentCommandResponseDTO;

public interface SegmentCommandService {

    SegmentCommandCreateDTO insertSegment(SegmentCommandCreateDTO dto);
    SegmentCommandResponseDTO updateSegment(Long segmentId, SegmentCommandResponseDTO dto);
    void deleteSegment(Long segmentId);
}