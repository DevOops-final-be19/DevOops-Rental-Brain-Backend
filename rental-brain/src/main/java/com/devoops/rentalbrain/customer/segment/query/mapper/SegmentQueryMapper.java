package com.devoops.rentalbrain.customer.segment.query.mapper;

import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryDetailDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryKPIDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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

    // 세그먼트 분석 KPI
    SegmentQueryKPIDTO selectSegmentKpi(
                                        @Param("monthStart") LocalDateTime monthStart,
                                        @Param("monthEnd") LocalDateTime monthEnd,
                                        @Param("prevStart") LocalDateTime prevStart,
                                        @Param("prevEnd") LocalDateTime prevEnd,
                                        @Param("lowStar") int lowStar,
                                        // 이건 이름으로 고정했음 -> 이탈 위험 고객
                                        @Param("riskSegmentName") String riskSegmentName);
}
