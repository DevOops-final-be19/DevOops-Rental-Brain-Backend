package com.devoops.rentalbrain.business.contract.command.repository;

import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ContractCommandRepository extends JpaRepository<ContractCommandEntity, Long> {


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    UPDATE ContractCommandEntity c
    SET c.status = 'E'
    WHERE c.status = 'P'
    AND FUNCTION(
        'DATE_ADD',
        c.startDate,
        CONCAT(c.contractPeriod, ' MONTH')
    ) BETWEEN :oneMonthLater AND :twoMonthsLater
""")
    int updateToExpireExpected(
            LocalDateTime oneMonthLater,
            LocalDateTime twoMonthsLater
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    UPDATE ContractCommandEntity c
    SET c.status = 'I'
    WHERE c.status = 'E'
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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    UPDATE ContractCommandEntity c
    SET c.status = 'C'
    WHERE c.status IN ('P', 'E', 'I')
    AND FUNCTION(
        'DATE_ADD',
        c.startDate,
        CONCAT(c.contractPeriod, ' MONTH')
    ) < :now
""")
    int updateToClosed(LocalDateTime now);
}
