package com.devoops.rentalbrain.business.campaign.query.controller;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.business.campaign.query.service.CouponQueryService;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@Slf4j
@Tag(name = "쿠폰 관리(Query)",
        description = "쿠폰 정보 조회 관련 API")
public class CouponQueryController {
    private final CouponQueryService couponQueryService;

    @Autowired
    public CouponQueryController(CouponQueryService couponQueryService) {
        this.couponQueryService = couponQueryService;
    }

    @GetMapping("/read-list")
    public ResponseEntity<PageResponseDTO<CouponDTO>> readCouponList(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);
        PageResponseDTO<CouponDTO> couponList = couponQueryService.readCouponList(criteria);
        return ResponseEntity.ok().body(couponList);
    }
}
