package com.devoops.rentalbrain.common.segmentrebuild.command.service;

public interface SegmentTransitionCommandService {

    /*
     * 계약 등록 커밋 성공 후 호출
     * - 잠재(1) → 신규(2)만 처리
     */
    void onContractCommitted(Long customerId, Long contractId);



}
