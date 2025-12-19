package com.devoops.rentalbrain.customer.segment.query.service;

import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryDetailDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryListDTO;
import com.devoops.rentalbrain.customer.segment.query.mapper.SegmentQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SegmentQueryServiceImpl implements SegmentQueryService{

    private final SegmentQueryMapper segmentQueryMapper;

    @Autowired
    public SegmentQueryServiceImpl(SegmentQueryMapper segmentQueryMapper) {
        this.segmentQueryMapper = segmentQueryMapper;
    }

    // 세그먼트 목록 조회
    @Override
    public List<SegmentQueryListDTO> selectSegmentList(String segmentName) {
        List<SegmentQueryListDTO> list = segmentQueryMapper.selectSegmentList(segmentName);

        log.info("세그먼트 목록 조회 - 갯수: {}개", list.size());
        return list;
    }

    // 세그먼트 상세 조회
    @Override
    public SegmentQueryDetailDTO selectSegmentDetail(Long segmentId) {
        SegmentQueryDetailDTO detail = segmentQueryMapper.selectSegmentDetail(segmentId);

        log.info("세그먼트 상세 조회 - segmentName: {}, segmentId: {}, customers: {}개의 회사",
                 detail.getSegmentName(),
                 detail.getSegmentId(),
                (detail.getCustomers() == null ? 0 : detail.getCustomers().size())
        );
        return detail;
    }
}
