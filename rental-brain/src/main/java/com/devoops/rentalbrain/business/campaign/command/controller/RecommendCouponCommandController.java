package com.devoops.rentalbrain.business.campaign.command.controller;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertRecommendCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.service.RecommendCouponCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommend/coupon/")
@Tag(name = "추천 쿠폰 관리(Command)",
        description = "추천 쿠폰 생성, 삭제 관련 API")
public class RecommendCouponCommandController {
    private final RecommendCouponCommandService recommendCouponCommandService;

    @Autowired
    public RecommendCouponCommandController(RecommendCouponCommandService recommendCouponCommandService) {
        this.recommendCouponCommandService = recommendCouponCommandService;
    }

    @Operation(
            summary = "추천 쿠폰 등록",
            description = "추천 쿠폰이 생성됩니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("insert")
    public void insertRecommendCoupon(@RequestBody InsertRecommendCouponDTO recommendCouponDTO) {
        recommendCouponCommandService.insertRecommendCoupon(recommendCouponDTO);
    }

    @Operation(
            summary = "추천 쿠폰 삭제",
            description = "추천 쿠폰을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @DeleteMapping("delete/{recommendCouponId}")
    public void DeleteRecommendCoupon(@PathVariable Long recommendCouponId) {
        recommendCouponCommandService.DeleteRecommendCoupon(recommendCouponId);
    }
}
