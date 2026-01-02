package com.devoops.rentalbrain.business.contract.command.repository;

import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ContractCommandRepository extends JpaRepository<ContractCommandEntity, Long> {

    /**
     * 진행중(P) → 만료임박(I)
     * 만료일이 1개월 이내 남았을 때
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            value = """
            UPDATE contract
            SET status = 'I'
            WHERE status = 'P'
              AND TIMESTAMPADD(
                    MONTH,
                    contract_period,
                    start_date
              ) BETWEEN :now AND :oneMonthLater
        """,
            nativeQuery = true
    )
    int updateToExpireImminent(
            @Param("now") LocalDateTime now,
            @Param("oneMonthLater") LocalDateTime oneMonthLater
    );

    /**
     * 진행중(P), 만료임박(I) → 만료(C)
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            value = """
            UPDATE contract
            SET status = 'C'
            WHERE id IN (:contractIds)
        """,
            nativeQuery = true
    )
    int updateToClosedByIds(
            @Param("contractIds") List<Long> contractIds
    );

    /**
     * 이미 만료된 계약 조회
     */
    @Query(
            value = """
            SELECT id
            FROM contract
            WHERE status IN ('P','I')
              AND TIMESTAMPADD(
                    MONTH,
                    contract_period,
                    start_date
              ) < :now
        """,
            nativeQuery = true
    )
    List<Long> findExpiredContractIds(
            @Param("now") LocalDateTime now
    );

    /**
     * 계약 만료 + 1개월 지난 계약 조회
     * (아이템 연체 처리 대상)
     */
    @Query(
            value = """
            SELECT id
            FROM contract
            WHERE status = 'C'
              AND TIMESTAMPADD(
                    MONTH,
                    contract_period + 1,
                    start_date
              ) < :now
        """,
            nativeQuery = true
    )
    List<Long> findContractsExpiredOneDayAgo(
            @Param("now") LocalDateTime now
    );
}
