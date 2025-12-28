package com.devoops.rentalbrain.common.segmentrebuild.command.repository;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SegmentRebuildBatchRepository extends JpaRepository<CustomerlistCommandEntity, Long> {

    // (A) 신규 -> 일반 대상 고객 목록
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

    // 잠재 -> 신규 (보정)
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

    // 신규 -> 일반 (승격)
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
