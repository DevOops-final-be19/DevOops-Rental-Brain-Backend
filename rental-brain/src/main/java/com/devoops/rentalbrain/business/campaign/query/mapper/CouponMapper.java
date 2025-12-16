package com.devoops.rentalbrain.business.campaign.query.mapper;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponMapper {
    List<CouponDTO> selectCoupon(int offset, int amount);

    long countCouponList();
}
