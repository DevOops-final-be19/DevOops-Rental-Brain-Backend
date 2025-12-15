package com.devoops.rentalbrain.business.contract.command.repository;

import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ContractCommandRepository extends JpaRepository<ContractCommandEntity, Long> {

    /**
     * 진행중(P) → 만료임박(I)
     * 만료일이 1개월 이내 남았을 때
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE ContractCommandEntity c
        SET c.status = 'I'
        WHERE c.status = 'P'
        AND FUNCTION(
            'DATE_ADD',
            c.startDate,
            CONCAT(c.contractPeriod, ' MONTH')
        ) BETWEEN :now AND :oneMonthLater
    """)
    int updateToExpireImminent(
            LocalDateTime now,
            LocalDateTime oneMonthLater
    );

    /**
     * 진행중(P), 만료임박(I) → 만료(C)
     * 만료일이 지난 계약
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE ContractCommandEntity c
        SET c.status = 'C'
        WHERE c.status IN ('P', 'I')
        AND FUNCTION(
            'DATE_ADD',
            c.startDate,
            CONCAT(c.contractPeriod, ' MONTH')
        ) < :now
    """)
    int updateToClosed(LocalDateTime now);
}
