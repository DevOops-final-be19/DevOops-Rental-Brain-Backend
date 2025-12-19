package com.devoops.rentalbrain.customer.segment.query.mapper;

import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryDetailDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SegmentQueryMapper {
    // 목록 조회
    List<SegmentQueryListDTO> selectSegmentList(@Param("segmentName") String segmentName);

    // 디테일 조회
    SegmentQueryDetailDTO selectSegmentDetail(@Param("segmentId") Long segmentId);

    // 고객목록 조회
    List<SegmentQueryDetailDTO.SegmentCustomerDTO>
        selectCustomersBySegmentId(@Param("segmentId") Long segmentId);
}
