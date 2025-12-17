package com.devoops.rentalbrain.customer.segment.query.service;

import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryDetailDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryKPIDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryListDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SegmentQueryService {
    List<SegmentQueryListDTO> selectSegmentList(String segmentName);

    SegmentQueryDetailDTO selectSegmentDetail(Long segmentId);

    SegmentQueryKPIDTO selectSegmentKpi(String month);
}
