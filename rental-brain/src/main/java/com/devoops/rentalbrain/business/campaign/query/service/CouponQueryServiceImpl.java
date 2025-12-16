package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.business.campaign.query.mapper.CouponMapper;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemNameDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CouponQueryServiceImpl implements CouponQueryService {
    private final CouponMapper couponMapper;

    @Autowired
    public CouponQueryServiceImpl(CouponMapper couponMapper) {
        this.couponMapper = couponMapper;
    }

    @Override
    public PageResponseDTO<CouponDTO> readCouponList(Criteria criteria) {
        List<CouponDTO> couponList = couponMapper.selectCoupon(criteria.getOffset(),
                criteria.getAmount());
        long totalCount = couponMapper.countCouponList();

        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        return new PageResponseDTO<>(couponList, totalCount, paging);
    }
}
