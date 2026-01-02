package com.devoops.rentalbrain.customer.customerlist.command.repository;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerlistCommandRepository extends JpaRepository<CustomerlistCommandEntity, Long> {

    // 잠재 -> 신규
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
    UPDATE 
           customer A
       SET A.segment_id = 2
     WHERE A.segment_id = 1
       AND EXISTS (
           SELECT 1
           FROM contract B
           WHERE B.cum_id = A.id
      )
    """, nativeQuery = true)
    int bulkPromotePotentialToNew();

    // 신규 -> 일반
    @Modifying
    @Query(value = """
    UPDATE customer c
    SET c.segment_id = 3
    WHERE c.segment_id = 2
      AND (
        SELECT MIN(ct.start_date)
        FROM contract ct
        WHERE ct.cum_id = c.id
      ) <= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
""", nativeQuery = true)
    int bulkPromoteNewToNormal();

    // 이력 남기는 것
    @Modifying
    @Query(value = """
    INSERT INTO customer_segment_history
    (customer_id, from_segment_id, to_segment_id, reason, trigger_type, reference_type, reference_id, changed_at, created_at)
    SELECT
        c.id                  AS customer_id,
        2                     AS from_segment_id,   -- 신규
        3                     AS to_segment_id,     -- 일반
        '첫 계약 후 3개월 경과' AS reason,
        'AUTO'                AS trigger_type,
        'CONTRACT'            AS reference_type,
        MIN(ct.id)            AS reference_id,
        NOW()                 AS changed_at,
        NOW()                 AS created_at
    FROM customer c
    JOIN contract ct ON ct.cum_id = c.id
    WHERE c.segment_id = 2
    GROUP BY c.id
    HAVING MIN(ct.start_date) <= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
""", nativeQuery = true)
    int insertHistoryNewToNormal();
}