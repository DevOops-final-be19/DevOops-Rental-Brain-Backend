package com.devoops.rentalbrain.business.contract.command.repository;

import com.devoops.rentalbrain.business.contract.command.entity.PaymentDetailCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentDetailCommandRepository extends JpaRepository<PaymentDetailCommandEntity,Long> {

    @Query("""
        SELECT p
        FROM PaymentDetailCommandEntity p
        WHERE p.paymentStatus = 'P'
          AND p.paymentActual IS NULL
          AND p.paymentDue < :now
    """)
    List<PaymentDetailCommandEntity> findExpiredUnpaid(
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Query("""
        update PaymentDetailCommandEntity p
        set p.overdueDays = p.overdueDays + 1
        where p.paymentStatus = 'N'
    """)
    int increaseOverdueDaysForNonPayment();
}
