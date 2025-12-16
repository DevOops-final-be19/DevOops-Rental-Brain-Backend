package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;

public interface CouponQueryService {
    PageResponseDTO<CouponDTO> readCouponList(Criteria criteria);
}
