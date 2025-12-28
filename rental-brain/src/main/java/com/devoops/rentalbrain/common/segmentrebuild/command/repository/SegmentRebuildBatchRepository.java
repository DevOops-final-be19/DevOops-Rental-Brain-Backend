package com.devoops.rentalbrain.common.segmentrebuild.command.repository;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SegmentRebuildBatchRepository extends JpaRepository<CustomerlistCommandEntity, Long> {

    /*
     * 신규(2) -> 일반(3) 대상 customer_id 목록 (이력 저장용)
     * 규칙:
     * - 신규(2)
     * - (해지 제외) 첫 계약 start_date 3개월 경과
     * - 활성 계약 존재 (end_date 없음 → start_date + contract_period)
     */
    @Query(value = """
        SELECT c.id
        FROM customer c
        JOIN (
            SELECT ct.cum_id, MIN(ct.start_date) AS first_start_date
            FROM `contract` ct
            WHERE ct.status <> 'T'
            GROUP BY ct.cum_id
        ) fc ON fc.cum_id = c.id
        WHERE c.segment_id = 2
          AND fc.first_start_date <= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
          AND EXISTS (
              SELECT 1
              FROM `contract` ct2
              WHERE ct2.cum_id = c.id
                AND ct2.status <> 'T'
                AND ct2.start_date <= CURDATE()
                AND DATE_ADD(ct2.start_date, INTERVAL ct2.contract_period MONTH) >= CURDATE()
          )
        """, nativeQuery = true)
    List<Long> findNewToNormalTargetCustomerIds();

    /*
     * 잠재(1) -> 신규(2) 보정 (계약 1건 이상 존재)
     */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 2
        WHERE c.segment_id = 1
          AND EXISTS (
              SELECT 1
              FROM `contract` ct
              WHERE ct.cum_id = c.id
          )
        """, nativeQuery = true)
    int bulkPromotePotentialToNew();

    /*
     * 신규(2) -> 일반(3) 승격 (대상 쿼리와 동일 조건)
     */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        JOIN (
            SELECT ct.cum_id, MIN(ct.start_date) AS first_start_date
            FROM `contract` ct
            WHERE ct.status <> 'T'
            GROUP BY ct.cum_id
        ) fc ON fc.cum_id = c.id
        SET c.segment_id = 3
        WHERE c.segment_id = 2
          AND fc.first_start_date <= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
          AND EXISTS (
              SELECT 1
              FROM `contract` ct2
              WHERE ct2.cum_id = c.id
                AND ct2.status <> 'T'
                AND ct2.start_date <= CURDATE()
                AND DATE_ADD(ct2.start_date, INTERVAL ct2.contract_period MONTH) >= CURDATE()
          )
        """, nativeQuery = true)
    int bulkPromoteNewToNormal();
}
