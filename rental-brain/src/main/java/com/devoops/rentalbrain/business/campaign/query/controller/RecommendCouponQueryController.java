package com.devoops.rentalbrain.business.campaign.query.controller;

import com.devoops.rentalbrain.business.campaign.query.dto.RecommendCouponDTO;
import com.devoops.rentalbrain.business.campaign.query.service.RecommendCouponQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommend/coupon/")
@Tag(name = "추천 쿠폰 관리(Query)",
        description = "추천 쿠폰 정보 조회 관련 API")
public class RecommendCouponQueryController {
    private final RecommendCouponQueryService recommendCouponQueryService;

    @Autowired
    public RecommendCouponQueryController(RecommendCouponQueryService recommendCouponQueryService) {
        this.recommendCouponQueryService = recommendCouponQueryService;
    }

    @Operation(
            summary = "추천 쿠폰 설문ID로 조회",
            description = "설문ID가 일치하는 추천쿠폰을 전체 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("all-read/{surveyId}")
    public ResponseEntity<List<RecommendCouponDTO>> recommendCouponsBySurveyId(@PathVariable Long surveyId) {
        List<RecommendCouponDTO> recommendCoupons = recommendCouponQueryService.recommendCouponsBySurveyId(surveyId);
        return ResponseEntity.ok().body(recommendCoupons);
    }

    @Operation(
            summary = "추천 쿠폰 개별 조회",
            description = "추천쿠폰을 개별 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("read-one/{recommendCouponId}")
    public ResponseEntity<RecommendCouponDTO> recommendCouponById(@PathVariable Long recommendCouponId) {
        RecommendCouponDTO recommendCoupon = recommendCouponQueryService.recommendCouponById(recommendCouponId);
        return ResponseEntity.ok().body(recommendCoupon);
    }

    @Operation(
            summary = "대시보드 추천 쿠폰 조회",
            description = "대시보드에서 추천쿠폰을 최신순으로 한개만 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("read-one")
    public ResponseEntity<RecommendCouponDTO> recommendCouponOne() {
        RecommendCouponDTO recommendCoupon = recommendCouponQueryService.recommendCouponOne();
        return ResponseEntity.ok().body(recommendCoupon);
    }
}
