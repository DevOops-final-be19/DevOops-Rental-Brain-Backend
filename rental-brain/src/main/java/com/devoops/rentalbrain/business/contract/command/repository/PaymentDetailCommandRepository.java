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

    @Modifying(clearAutomatically = true)
    @Query("""
    update PaymentDetailCommandEntity p
    set p.overdueDays = COALESCE(p.overdueDays, 0) + 1
    where p.paymentStatus = 'N'
""")
    int increaseOverdueDaysForNonPayment();

    @Modifying(clearAutomatically = true)
    @Query("""
    update PaymentDetailCommandEntity p
    set p.paymentStatus = 'N',
        p.overdueDays = 0
    where p.paymentStatus = 'P'
      and p.paymentActual is null
      and p.paymentDue < :now
""")
    int markAsNonPayment(@Param("now") LocalDateTime now);

    @Modifying
    @Query("""
    UPDATE PaymentDetailCommandEntity p
       SET p.contractId=NULL
     WHERE p.contractId=:contractId
""")
    void detachPaymentDetail(Long contractId);
}
