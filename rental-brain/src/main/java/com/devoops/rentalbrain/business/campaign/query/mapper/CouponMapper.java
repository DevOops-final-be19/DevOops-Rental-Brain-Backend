package com.devoops.rentalbrain.business.campaign.query.mapper;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.CouponWithContractDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponMapper {
    List<CouponDTO> selectCoupon(int offset, int amount);

    long countCouponList();

    List<CouponDTO> searchCoupon(String keyword, int offset, int amount);

    long countSearchedCoupon(String keyword);

    List<CouponDTO> filteringCouponByType(String type, int offset, int amount);

    long countFliteringCoupon(String type);

    List<CouponDTO> filteringCouponByStatus(String status, int offset, int amount);

    long countFliterCouponByStatus(String status);

    List<CouponWithContractDTO> useContractCoupon(String segment);
}
