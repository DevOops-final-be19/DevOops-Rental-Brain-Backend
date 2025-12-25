package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.CouponWithContractDTO;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;

import java.util.List;

public interface CouponQueryService {
    PageResponseDTO<CouponDTO> readCouponList(Criteria criteria);

    PageResponseDTO<CouponDTO> searchCoupon(String keyword, Criteria criteria);

    PageResponseDTO<CouponDTO> filterCouponByType(String type, Criteria criteria);

    PageResponseDTO<CouponDTO> filterCouponByStatus(String status, Criteria criteria);

    List<CouponWithContractDTO> useContractCoupon(Integer segment);

    CouponDTO readDetailCoupon(String couCode);
}
