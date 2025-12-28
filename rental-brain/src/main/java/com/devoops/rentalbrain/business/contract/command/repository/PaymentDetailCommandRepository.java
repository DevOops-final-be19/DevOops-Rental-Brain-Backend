package com.devoops.rentalbrain.business.contract.command.repository;

import com.devoops.rentalbrain.business.contract.command.entity.PaymentDetailCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PaymentDetailCommandRepository extends JpaRepository<PaymentDetailCommandEntity,Long> {

    @Modifying
    @Query("""
        update PaymentDetailCommandEntity p
        set p.overdueDays = p.overdueDays + 1
        where p.paymentStatus = 'N'
    """)
    int increaseOverdueDaysForNonPayment();
}
