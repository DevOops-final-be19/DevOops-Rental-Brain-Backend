package com.devoops.rentalbrain.common.segmentrebuild.command.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SegmentTransitionCommandMapper {

    // 고객의 계약 건수
    int countContracts(@Param("customerId") Long customerId);

    // 고객의 현재 세그먼트 ID 조회
    Long findCurrentSegmentId(@Param("customerId") Long customerId);

    // 잠재(1) → 신규(2)로 승격, @return 업데이트된 row 수 (1이면 전이 성공, 0이면 미전이)
    int promotePotentialToNew(@Param("customerId") Long customerId);

    // 세그먼트 변경 이력 저장
    int insertSegmentHistory(@Param("customerId") Long customerId,
                             @Param("previousSegmentId") Long previousSegmentId,
                             @Param("currentSegmentId") Long currentSegmentId,
                             @Param("reason") String reason,
                             @Param("triggerType") String triggerType,
                             @Param("referenceType") String referenceType,
                             @Param("referenceId") Long referenceId);

    void updateCustomerSegment(Long customerId, Long targetSegmentId);
}
