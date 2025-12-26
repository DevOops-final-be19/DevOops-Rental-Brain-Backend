package com.devoops.rentalbrain.business.campaign.query.mapper;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.CouponWithContractDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CouponMapper {
    List<CouponDTO> selectCoupon(@Param("offset") int offset, @Param("limit") int limit);

    long countCouponList();

    List<CouponDTO> searchCoupon(String keyword,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countSearchedCoupon(String keyword);

    List<CouponDTO> filteringCouponByType(String type,
                                          @Param("offset") int offset,
                                          @Param("limit") int limit);

    long countFliteringCoupon(String type);

    List<CouponDTO> filteringCouponByStatus(String status,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    long countFliterCouponByStatus(String status);

    List<CouponWithContractDTO> useContractCoupon(Integer segment);

    CouponDTO selectEachCoupon(String couCode);
}
