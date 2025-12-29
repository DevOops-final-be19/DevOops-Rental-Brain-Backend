package com.devoops.rentalbrain.common.segmentrebuild.command.repository;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SegmentRebuildBatchRepository extends JpaRepository<CustomerlistCommandEntity, Long> {

    // 잠재 -> 신규
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 2
        WHERE c.segment_id = 1
          AND EXISTS (SELECT 1 FROM `contract` ct WHERE ct.cum_id = c.id)
        """, nativeQuery = true)
    int bulkPromotePotentialToNew();

    // 신규 -> 일반 대상 조회
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

    // 신규 -> 일반 벌크 업데이트
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

    // 일반 -> VIP 대상 조회
    @Query(value = """
        SELECT c.id
        FROM customer c
        JOIN `contract` ct ON ct.cum_id = c.id
        WHERE c.segment_id = 3
          AND ct.status <> 'T'
        GROUP BY c.id
        HAVING SUM(ct.contract_period) >= 36
            OR SUM(ct.total_amount) >= 300000000
        """, nativeQuery = true)
    List<Long> findNormalToVipTargetCustomerIds();

    // 일반 -> VIP 벌크 업데이트
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 5
        WHERE c.segment_id = 3
          AND c.id IN (
              SELECT ct.cum_id
              FROM `contract` ct
              WHERE ct.status <> 'T'
              GROUP BY ct.cum_id
              HAVING SUM(ct.contract_period) >= 36
                  OR SUM(ct.total_amount) >= 300000000
          )
        """, nativeQuery = true)
    int bulkPromoteNormalToVip();

    /* ===============================
       이탈위험(4) 대상 조회
       =============================== */
    interface RiskTargetRow {
        Long getCustomerId();
        Long getFromSegmentId();
        String getReasonCode();
    }

    @Query(value = """
        SELECT
            c.id AS customerId,
            c.segment_id AS fromSegmentId,
            CASE
                WHEN EXISTS (
                    SELECT 1
                    FROM `contract` ct
                    WHERE ct.cum_id = c.id
                      AND ct.status = 'T'
                ) THEN 'TERMINATION'

                WHEN EXISTS (
                    SELECT 1
                    FROM `contract` ct
                    WHERE ct.cum_id = c.id
                      AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH)
                          BETWEEN DATE_ADD(CURDATE(), INTERVAL 1 MONTH)
                              AND DATE_ADD(CURDATE(), INTERVAL 3 MONTH)
                ) THEN 'EXPIRING_1_3M'

                WHEN EXISTS (
                    SELECT 1
                    FROM (
                        SELECT cum_id, MAX(overdue_period) AS max_overdue
                        FROM pay_overdue
                        WHERE due_date < CURDATE()
                          AND (status IS NULL OR status <> 'C')
                        GROUP BY cum_id
                        UNION ALL
                        SELECT cum_id, MAX(overdue_period)
                        FROM item_overdue
                        WHERE due_date < CURDATE()
                          AND (status IS NULL OR status <> 'C')
                        GROUP BY cum_id
                    ) od
                    WHERE od.cum_id = c.id
                      AND od.max_overdue BETWEEN 1 AND 89
                ) THEN 'OVERDUE_LT_3M'

                WHEN (
                    SELECT AVG(f.star)
                    FROM feedback f
                    WHERE f.cum_id = c.id
                      AND f.star IS NOT NULL
                      AND f.create_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH)
                ) <= 2.5 THEN 'LOW_SAT'

                WHEN (
                    NOT EXISTS (
                        SELECT 1
                        FROM `contract` ct
                        WHERE ct.cum_id = c.id
                          AND ct.start_date <= CURDATE()
                          AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH) >= CURDATE()
                    )
                    AND (
                        SELECT MAX(DATE_ADD(ct2.start_date, INTERVAL ct2.contract_period MONTH))
                        FROM `contract` ct2
                        WHERE ct2.cum_id = c.id
                    ) BETWEEN DATE_SUB(CURDATE(), INTERVAL 3 MONTH) AND CURDATE()
                ) THEN 'ENDED_WITHIN_3M'

                ELSE 'NONE'
            END AS reasonCode
        FROM customer c
        WHERE c.segment_id IN (2,3,5,7)
        HAVING reasonCode <> 'NONE'
        """, nativeQuery = true)
    List<RiskTargetRow> findToRiskTargets();

    /* ===============================
       이탈위험(4) 벌크 업데이트
       =============================== */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 4
        WHERE c.segment_id IN (2,3,5,7)
          AND (
              EXISTS (
                  SELECT 1 FROM `contract` ct
                  WHERE ct.cum_id = c.id AND ct.status = 'T'
              )
              OR EXISTS (
                  SELECT 1 FROM `contract` ct
                  WHERE ct.cum_id = c.id
                    AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH)
                        BETWEEN DATE_ADD(CURDATE(), INTERVAL 1 MONTH)
                            AND DATE_ADD(CURDATE(), INTERVAL 3 MONTH)
              )
              OR EXISTS (
                  SELECT 1
                  FROM (
                      SELECT cum_id, MAX(overdue_period) AS max_overdue
                      FROM pay_overdue
                      WHERE due_date < CURDATE()
                        AND (status IS NULL OR status <> 'C')
                      GROUP BY cum_id
                      UNION ALL
                      SELECT cum_id, MAX(overdue_period)
                      FROM item_overdue
                      WHERE due_date < CURDATE()
                        AND (status IS NULL OR status <> 'C')
                      GROUP BY cum_id
                  ) od
                  WHERE od.cum_id = c.id
                    AND od.max_overdue BETWEEN 1 AND 89
              )
              OR (
                  SELECT AVG(f.star)
                  FROM feedback f
                  WHERE f.cum_id = c.id
                    AND f.star IS NOT NULL
                    AND f.create_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH)
              ) <= 2.5
              OR (
                  NOT EXISTS (
                      SELECT 1 FROM `contract` ct
                      WHERE ct.cum_id = c.id
                        AND ct.start_date <= CURDATE()
                        AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH) >= CURDATE()
                  )
                  AND (
                      SELECT MAX(DATE_ADD(ct2.start_date, INTERVAL ct2.contract_period MONTH))
                      FROM `contract` ct2
                      WHERE ct2.cum_id = c.id
                  ) BETWEEN DATE_SUB(CURDATE(), INTERVAL 3 MONTH) AND CURDATE()
              )
          )
        """, nativeQuery = true)
    int bulkPromoteToRisk();

    /* ===============================
       블랙리스트 대상 조회
       =============================== */
    interface BlacklistTargetRow {
        Long getCustomerId();
        Long getReferenceId();
        String getReferenceType();
        Integer getMaxOverduePeriod();
    }

    @Query(value = """
        SELECT t.customer_id AS customerId,
               t.reference_id AS referenceId,
               t.reference_type AS referenceType,
               t.max_overdue AS maxOverduePeriod
        FROM (
            SELECT po.cum_id AS customer_id,
                   MAX(po.id) AS reference_id,
                   'PAY_OVERDUE' AS reference_type,
                   MAX(po.overdue_period) AS max_overdue
            FROM pay_overdue po
            WHERE po.due_date < CURDATE()
              AND (po.status IS NULL OR po.status <> 'C')
            GROUP BY po.cum_id

            UNION ALL

            SELECT io.cum_id AS customer_id,
                   MAX(io.id) AS reference_id,
                   'ITEM_OVERDUE' AS reference_type,
                   MAX(io.overdue_period) AS max_overdue
            FROM item_overdue io
            WHERE io.due_date < CURDATE()
              AND (io.status IS NULL OR io.status <> 'C')
            GROUP BY io.cum_id
        ) t
        JOIN customer c ON c.id = t.customer_id
        WHERE c.segment_id = 4
          AND c.is_deleted = 'N'
          AND t.max_overdue >= 90
        """, nativeQuery = true)
    List<BlacklistTargetRow> findRiskToBlacklistTargets();

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 6
        WHERE c.segment_id = 4
          AND (
              EXISTS (
                  SELECT 1 FROM pay_overdue po
                  WHERE po.cum_id = c.id
                    AND po.due_date < CURDATE()
                    AND (po.status IS NULL OR po.status <> 'C')
                    AND po.overdue_period >= 90
              )
              OR EXISTS (
                  SELECT 1 FROM item_overdue io
                  WHERE io.cum_id = c.id
                    AND io.due_date < CURDATE()
                    AND (io.status IS NULL OR io.status <> 'C')
                    AND io.overdue_period >= 90
              )
          )
        """, nativeQuery = true)
    int bulkPromoteRiskToBlacklist();

    /* ===============================
       확장 의사 고객(7) 대상 조회/업데이트
       =============================== */
    interface ExpansionTargetRow {
        Long getCustomerId();
        Long getFromSegmentId();
        String getReasonCode();
    }

    @Query(value = """
    SELECT
        c.id AS customerId,
        c.segment_id AS fromSegmentId,
        CASE
            WHEN (
                -- 업셀링: 최근 3개월 합계 >= 직전 3개월 합계 * 1.2
                (
                    SELECT COALESCE(SUM(ct.total_amount), 0)
                    FROM `contract` ct
                    WHERE ct.cum_id = c.id
                      AND ct.status <> 'T'
                      AND ct.start_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
                ) >=
                (
                    SELECT COALESCE(SUM(ct2.total_amount), 0)
                    FROM `contract` ct2
                    WHERE ct2.cum_id = c.id
                      AND ct2.status <> 'T'
                      AND ct2.start_date < DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
                      AND ct2.start_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
                ) * 1.2
                AND (
                    SELECT COALESCE(SUM(ct2.total_amount), 0)
                    FROM `contract` ct2
                    WHERE ct2.cum_id = c.id
                      AND ct2.status <> 'T'
                      AND ct2.start_date < DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
                      AND ct2.start_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
                ) > 0
            ) THEN 'UPSALE_GROWTH_20P'

            WHEN (
                -- 재계약+확장: 만료 3~6개월 전 + 최근3개월 만족도 >= 4.0 + 최소 1건
                EXISTS (
                    SELECT 1
                    FROM `contract` ct
                    WHERE ct.cum_id = c.id
                      AND ct.status <> 'T'
                      AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH)
                          BETWEEN DATE_ADD(CURDATE(), INTERVAL 3 MONTH)
                              AND DATE_ADD(CURDATE(), INTERVAL 6 MONTH)
                )
                AND (
                    SELECT AVG(f.star)
                    FROM feedback f
                    WHERE f.cum_id = c.id
                      AND f.star IS NOT NULL
                      AND f.create_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH)
                ) >= 4.0
                AND EXISTS (
                    SELECT 1
                    FROM feedback f2
                    WHERE f2.cum_id = c.id
                      AND f2.star IS NOT NULL
                      AND f2.create_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH)
                )
            ) THEN 'RENEWAL_3_6M_HIGH_SAT'

            ELSE 'NONE'
        END AS reasonCode
    FROM customer c
    WHERE c.segment_id IN (2,3,5)     -- 확장 후보(신규/일반/VIP)에서만 올리는 정책
      AND c.is_deleted = 'N'
      -- 안전장치: 해지요청/연체(1일+)는 확장 제외
      AND NOT EXISTS (
          SELECT 1 FROM `contract` ct
          WHERE ct.cum_id = c.id AND ct.status = 'T'
      )
      AND NOT EXISTS (
          SELECT 1
          FROM (
              SELECT cum_id, MAX(overdue_period) AS max_overdue
              FROM pay_overdue
              WHERE due_date < CURDATE()
                AND (status IS NULL OR status <> 'C')
              GROUP BY cum_id
              UNION ALL
              SELECT cum_id, MAX(overdue_period)
              FROM item_overdue
              WHERE due_date < CURDATE()
                AND (status IS NULL OR status <> 'C')
              GROUP BY cum_id
          ) od
          WHERE od.cum_id = c.id
            AND od.max_overdue >= 1
      )
    HAVING reasonCode <> 'NONE'
    """, nativeQuery = true)
    List<ExpansionTargetRow> findToExpansionTargets();

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
    UPDATE customer c
    SET c.segment_id = 7
    WHERE c.segment_id IN (2,3,5)
      AND c.is_deleted = 'N'
      AND NOT EXISTS (
          SELECT 1 FROM `contract` ct
          WHERE ct.cum_id = c.id AND ct.status = 'T'
      )
      AND NOT EXISTS (
          SELECT 1
          FROM (
              SELECT cum_id, MAX(overdue_period) AS max_overdue
              FROM pay_overdue
              WHERE due_date < CURDATE()
                AND (status IS NULL OR status <> 'C')
              GROUP BY cum_id
              UNION ALL
              SELECT cum_id, MAX(overdue_period)
              FROM item_overdue
              WHERE due_date < CURDATE()
                AND (status IS NULL OR status <> 'C')
              GROUP BY cum_id
          ) od
          WHERE od.cum_id = c.id
            AND od.max_overdue >= 1
      )
      AND (
          (
              (
                  SELECT COALESCE(SUM(ct.total_amount), 0)
                  FROM `contract` ct
                  WHERE ct.cum_id = c.id
                    AND ct.status <> 'T'
                    AND ct.start_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
              ) >=
              (
                  SELECT COALESCE(SUM(ct2.total_amount), 0)
                  FROM `contract` ct2
                  WHERE ct2.cum_id = c.id
                    AND ct2.status <> 'T'
                    AND ct2.start_date < DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
                    AND ct2.start_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
              ) * 1.2
              AND (
                  SELECT COALESCE(SUM(ct2.total_amount), 0)
                  FROM `contract` ct2
                  WHERE ct2.cum_id = c.id
                    AND ct2.status <> 'T'
                    AND ct2.start_date < DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
                    AND ct2.start_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
              ) > 0
          )
          OR (
              EXISTS (
                  SELECT 1
                  FROM `contract` ct
                  WHERE ct.cum_id = c.id
                    AND ct.status <> 'T'
                    AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH)
                        BETWEEN DATE_ADD(CURDATE(), INTERVAL 3 MONTH)
                            AND DATE_ADD(CURDATE(), INTERVAL 6 MONTH)
              )
              AND (
                  SELECT AVG(f.star)
                  FROM feedback f
                  WHERE f.cum_id = c.id
                    AND f.star IS NOT NULL
                    AND f.create_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH)
              ) >= 4.0
              AND EXISTS (
                  SELECT 1
                  FROM feedback f2
                  WHERE f2.cum_id = c.id
                    AND f2.star IS NOT NULL
                    AND f2.create_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH)
              )
          )
      )
    """, nativeQuery = true)
    int bulkPromoteToExpansion();

    // 이탈위험 고객 -> 일반
    interface RiskToNormalTargetRow {
        Long getCustomerId();
        Long getFromSegmentId();
        String getReasonCode();  // 선택: 복귀 사유 코드
    }

    @Query(value = """
    SELECT
        c.id AS customerId,
        c.segment_id AS fromSegmentId,
        'RISK_CLEARED' AS reasonCode
    FROM customer c
    WHERE c.segment_id = 4
      AND c.is_deleted = 'N'

      -- 1) 해지 요청 없음
      AND NOT EXISTS (
          SELECT 1
          FROM `contract` ct
          WHERE ct.cum_id = c.id
            AND ct.status = 'T'
      )

      -- 2) 연체 없음(1일 이상 연체가 남아있으면 제외)
      AND NOT EXISTS (
          SELECT 1
          FROM (
              SELECT cum_id, MAX(overdue_period) AS max_overdue
              FROM pay_overdue
              WHERE due_date < CURDATE()
                AND (status IS NULL OR status <> 'C')
              GROUP BY cum_id
              UNION ALL
              SELECT cum_id, MAX(overdue_period)
              FROM item_overdue
              WHERE due_date < CURDATE()
                AND (status IS NULL OR status <> 'C')
              GROUP BY cum_id
          ) od
          WHERE od.cum_id = c.id
            AND od.max_overdue >= 1
      )

      -- 3) 활성 계약 1건 이상
      AND EXISTS (
          SELECT 1
          FROM `contract` ct
          WHERE ct.cum_id = c.id
            AND ct.status <> 'T'
            AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH) >= CURDATE()
      )

      -- 4) (선택) 만료 1~3개월 임박이 있으면 복귀 안 시킴 (안정성)
      AND NOT EXISTS (
          SELECT 1
          FROM `contract` ct
          WHERE ct.cum_id = c.id
            AND ct.status <> 'T'
            AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH)
                BETWEEN DATE_ADD(CURDATE(), INTERVAL 1 MONTH)
                    AND DATE_ADD(CURDATE(), INTERVAL 3 MONTH)
      )
    """, nativeQuery = true)
    List<RiskToNormalTargetRow> findRiskToNormalTargets();

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
    UPDATE customer c
    SET c.segment_id = 3
    WHERE c.segment_id = 4
      AND c.is_deleted = 'N'
      AND NOT EXISTS (
          SELECT 1
          FROM `contract` ct
          WHERE ct.cum_id = c.id
            AND ct.status = 'T'
      )
      AND NOT EXISTS (
          SELECT 1
          FROM (
              SELECT cum_id, MAX(overdue_period) AS max_overdue
              FROM pay_overdue
              WHERE due_date < CURDATE()
                AND (status IS NULL OR status <> 'C')
              GROUP BY cum_id
              UNION ALL
              SELECT cum_id, MAX(overdue_period)
              FROM item_overdue
              WHERE due_date < CURDATE()
                AND (status IS NULL OR status <> 'C')
              GROUP BY cum_id
          ) od
          WHERE od.cum_id = c.id
            AND od.max_overdue >= 1
      )
      AND EXISTS (
          SELECT 1
          FROM `contract` ct
          WHERE ct.cum_id = c.id
            AND ct.status <> 'T'
            AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH) >= CURDATE()
      )
      AND NOT EXISTS (
          SELECT 1
          FROM `contract` ct
          WHERE ct.cum_id = c.id
            AND ct.status <> 'T'
            AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH)
                BETWEEN DATE_ADD(CURDATE(), INTERVAL 1 MONTH)
                    AND DATE_ADD(CURDATE(), INTERVAL 3 MONTH)
      )
    """, nativeQuery = true)
    int bulkDemoteRiskToNormal();
}
