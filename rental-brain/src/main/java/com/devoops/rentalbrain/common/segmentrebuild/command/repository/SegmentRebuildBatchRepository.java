package com.devoops.rentalbrain.common.segmentrebuild.command.repository;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SegmentRebuildBatchRepository extends JpaRepository<CustomerlistCommandEntity, Long> {

    /* =========================================================
       1) 잠재(1) → 신규(2)
       - 계약 이력 없음 → 첫 계약 등록(= 계약 존재하면 신규로)
       ========================================================= */

    @Query(value = """
        SELECT c.id
        FROM customer c
        WHERE c.segment_id = 1
          AND c.is_deleted = 'N'
          AND c.id NOT IN (:excludeIds)
          AND EXISTS (
              SELECT 1
              FROM `contract` ct
              WHERE ct.cum_id = c.id
          )
        """, nativeQuery = true)
    List<Long> findPotentialToNewTargetCustomerIdsExcluding(@Param("excludeIds") List<Long> excludeIds);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 2
        WHERE c.id IN (:customerIds)
          AND c.segment_id = 1
          AND c.is_deleted = 'N'
        """, nativeQuery = true)
    int bulkPromotePotentialToNewByIds(@Param("customerIds") List<Long> customerIds);


    /* =========================================================
       2) 신규(2) → 일반(3)
       - 신규 상태
       - 첫 계약 시작 후 3개월 경과 (해지 제외)
       - 활성 계약 ≥ 1 (end = start + period, 해지 제외)
       ========================================================= */

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
          AND c.is_deleted = 'N'
          AND c.id NOT IN (:excludeIds)
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
    List<Long> findNewToNormalTargetCustomerIdsExcluding(@Param("excludeIds") List<Long> excludeIds);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 3
        WHERE c.id IN (:customerIds)
          AND c.segment_id = 2
          AND c.is_deleted = 'N'
        """, nativeQuery = true)
    int bulkPromoteNewToNormalByIds(@Param("customerIds") List<Long> customerIds);


    /* =========================================================
       3) 일반(3) → VIP(5)
       - 계약 유지 개월 수 합계 ≥ 36개월 OR 총 계약금액 ≥ 3억
       - 해지 제외
       ※ OR 유지(요구대로) — 나중에 AND로 변경 예정
       ========================================================= */

    interface VipTargetRow {
        Long getCustomerId();
        Integer getSumMonths();
        Long getSumAmount();
        Integer getByMonths();
        Integer getByAmount();
    }

    @Query(value = """
    SELECT
        c.id AS customerId,
        SUM(ct.contract_period) AS sumMonths,
        SUM(ct.total_amount) AS sumAmount,
        CASE WHEN SUM(ct.contract_period) >= 36 THEN 1 ELSE 0 END AS byMonths,
        CASE WHEN SUM(ct.total_amount) >= 300000000 THEN 1 ELSE 0 END AS byAmount
    FROM customer c
    JOIN `contract` ct ON ct.cum_id = c.id
    WHERE c.segment_id = 3
      AND c.is_deleted = 'N'
      AND c.id NOT IN (:excludeIds)
      AND ct.status <> 'T'
    GROUP BY c.id
    HAVING byMonths = 1 OR byAmount = 1
    """, nativeQuery = true)
    List<VipTargetRow> findNormalToVipTargetsExcluding(@Param("excludeIds") List<Long> excludeIds);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 5
        WHERE c.id IN (:customerIds)
          AND c.segment_id = 3
          AND c.is_deleted = 'N'
        """, nativeQuery = true)
    int bulkPromoteNormalToVipByIds(@Param("customerIds") List<Long> customerIds);


    /* =========================================================
       4) 고객(2,3,5,7) → 이탈위험(4)
       - 만료 임박(1~3M)
       - 해지 요청(status='T')
       - 연체(1~89)
       - 최근 3개월 평균 star <= 2.5 (최소 1건 존재 권장)
       - 계약 종료 후 3개월 이내 & 활성 계약 없음
       ========================================================= */

    interface RiskTargetRow {
        Long getCustomerId();
        Long getFromSegmentId();

        Integer getHasTermination();
        Integer getHasLowSat();
        Integer getHasExpiring13m();
        Integer getHasOverdueLt3m();
        Integer getHasEndedWithin3m();
    }

    @Query(value = """
    SELECT
        c.id AS customerId,
        c.segment_id AS fromSegmentId,

        CASE WHEN EXISTS (
            SELECT 1 FROM `contract` ct
            WHERE ct.cum_id = c.id
              AND ct.status = 'T'
        ) THEN 1 ELSE 0 END AS hasTermination,

        CASE WHEN EXISTS (
            SELECT 1 FROM `contract` ct
            WHERE ct.cum_id = c.id
              AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH)
                  BETWEEN DATE_ADD(CURDATE(), INTERVAL 1 MONTH)
                      AND DATE_ADD(CURDATE(), INTERVAL 3 MONTH)
        ) THEN 1 ELSE 0 END AS hasExpiring13m,

        CASE WHEN EXISTS (
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
        ) THEN 1 ELSE 0 END AS hasOverdueLt3m,

        CASE WHEN (
            (SELECT AVG(f.star)
             FROM feedback f
             WHERE f.cum_id = c.id
               AND f.star IS NOT NULL
               AND f.create_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH)
            ) <= 2.5
            AND EXISTS (
                SELECT 1 FROM feedback fx
                WHERE fx.cum_id = c.id
                  AND fx.star IS NOT NULL
                  AND fx.create_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH)
            )
        ) THEN 1 ELSE 0 END AS hasLowSat,

        CASE WHEN (
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
        ) THEN 1 ELSE 0 END AS hasEndedWithin3m

    FROM customer c
    WHERE c.segment_id IN (2,3,5,7)
      AND c.is_deleted = 'N'
      AND c.id NOT IN (:excludeIds)

    HAVING (hasTermination + hasExpiring13m + hasOverdueLt3m + hasLowSat + hasEndedWithin3m) > 0
        """, nativeQuery = true)
    List<RiskTargetRow> findToRiskTargetsExcluding(@Param("excludeIds") List<Long> excludeIds);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 4
        WHERE c.id IN (:customerIds)
          AND c.segment_id IN (2,3,5,7)
          AND c.is_deleted = 'N'
        """, nativeQuery = true)
    int bulkPromoteToRiskByIds(@Param("customerIds") List<Long> customerIds);


    /* =========================================================
       5) 이탈위험(4) → 블랙리스트(6)
       - 연체 90일 이상
       ========================================================= */

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
          AND c.id NOT IN (:excludeIds)
          AND t.max_overdue >= 90
        """, nativeQuery = true)
    List<BlacklistTargetRow> findRiskToBlacklistTargetsExcluding(@Param("excludeIds") List<Long> excludeIds);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 6
        WHERE c.id IN (:customerIds)
          AND c.segment_id = 4
          AND c.is_deleted = 'N'
        """, nativeQuery = true)
    int bulkPromoteRiskToBlacklistByIds(@Param("customerIds") List<Long> customerIds);


    /* =========================================================
       6) 고객(2,3,5) → 확장 의사(7)
       - (예시 기준) 업셀 성장 20% OR 만료 3~6M + 최근 3개월 평균 4.0 이상
       - 위험(해지/연체/블랙 성격) 있으면 제외
       ========================================================= */

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
        WHERE c.segment_id IN (2,3,5)
          AND c.is_deleted = 'N'
          AND c.id NOT IN (:excludeIds)

          -- 해지 요청 있으면 제외
          AND NOT EXISTS (
              SELECT 1 FROM `contract` ct
              WHERE ct.cum_id = c.id AND ct.status = 'T'
          )

          -- 연체 있으면 제외(1일 이상)
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
    List<ExpansionTargetRow> findToExpansionTargetsExcluding(@Param("excludeIds") List<Long> excludeIds);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 7
        WHERE c.id IN (:customerIds)
          AND c.segment_id IN (2,3,5)
          AND c.is_deleted = 'N'
        """, nativeQuery = true)
    int bulkPromoteToExpansionByIds(@Param("customerIds") List<Long> customerIds);


    /* =========================================================
       7) 이탈위험(4) → 일반(3) 복귀
       - "계약을 하면 일반으로 돌아오는" 규칙
       - 해지요청/연체는 없어야 함
       - 활성 계약 1건 이상 존재
       ========================================================= */

    interface RiskToNormalTargetRow {
        Long getCustomerId();
        Long getFromSegmentId();
        String getReasonCode();
    }

    @Query(value = """
        SELECT
            c.id AS customerId,
            c.segment_id AS fromSegmentId,
            'RISK_CLEARED' AS reasonCode
        FROM customer c
        WHERE c.segment_id = 4
          AND c.is_deleted = 'N'
          AND c.id NOT IN (:excludeIds)

          -- 해지요청이 있으면 복귀 금지
          AND NOT EXISTS (
              SELECT 1
              FROM `contract` ct
              WHERE ct.cum_id = c.id
                AND ct.status = 'T'
          )

          -- 연체 있으면 복귀 금지(1일 이상)
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

          -- 활성 계약 존재하면 복귀
          AND EXISTS (
              SELECT 1
              FROM `contract` ct
              WHERE ct.cum_id = c.id
                AND ct.status <> 'T'
                AND ct.start_date <= CURDATE()
                AND DATE_ADD(ct.start_date, INTERVAL ct.contract_period MONTH) >= CURDATE()
          )
        """, nativeQuery = true)
    List<RiskToNormalTargetRow> findRiskToNormalTargetsExcluding(@Param("excludeIds") List<Long> excludeIds);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE customer c
        SET c.segment_id = 3
        WHERE c.id IN (:customerIds)
          AND c.segment_id = 4
          AND c.is_deleted = 'N'
        """, nativeQuery = true)
    int bulkDemoteRiskToNormalByIds(@Param("customerIds") List<Long> customerIds);
}
