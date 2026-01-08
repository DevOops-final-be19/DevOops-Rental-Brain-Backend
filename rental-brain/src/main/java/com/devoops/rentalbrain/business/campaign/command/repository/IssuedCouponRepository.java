package com.devoops.rentalbrain.business.campaign.command.repository;

import com.devoops.rentalbrain.business.campaign.command.entity.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {
    IssuedCoupon findByConId(Long contractId);

    boolean existsByConIdAndIsUsed(Long contractId, String n);

    @Modifying
    @Query("""
    UPDATE IssuedCoupon i
       SET i.conId=NULL
     WHERE i.conId=:contractId
""")
    void detachIssuedCoupon(Long contractId);
}
