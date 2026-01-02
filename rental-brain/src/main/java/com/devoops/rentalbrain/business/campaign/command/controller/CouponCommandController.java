package com.devoops.rentalbrain.business.campaign.command.controller;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.dto.ModifyCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.service.CouponCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@Slf4j
@Tag(name = "쿠폰 관리(Command)",
        description = "쿠폰 등록, 수정, 삭제 관련 API")
public class CouponCommandController {
    private final CouponCommandService couponCommandService;

    @Autowired
    public CouponCommandController(CouponCommandService couponCommandService) {
        this.couponCommandService = couponCommandService;
    }

    @Operation(
            summary = "쿠폰 등록",
            description = "쿠폰을 등록합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("/insert")
    public String insertCoupon(@RequestBody InsertCouponDTO couponDTO) {
        String result = couponCommandService.insertCoupon(couponDTO);

        return result;
    }

    @Operation(
            summary = "쿠폰 정보 수정",
            description = "쿠폰의 정보를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PutMapping("/update/{couCode}")
    public String updateCoupon(@PathVariable String couCode, @RequestBody ModifyCouponDTO couponDTO) {
        String result = couponCommandService.updateCoupon(couCode, couponDTO);

        return result;
    }

    @Operation(
            summary = "쿠폰 삭제",
            description = "쿠폰을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @DeleteMapping("/delete/{couCode}")
    public String deleteCoupon(@PathVariable("couCode") String couCode) {
        String result = couponCommandService.deleteCoupon(couCode);
        return result;
    }


    @Operation(
            summary = "쿠폰 사용 이력",
            description = "계약 시 선택된 쿠폰으로 사용 이력을 남깁니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("/log/{couponId}/{contractId}")
    public String createIssuedCoupon(@PathVariable("couponId") Long couponId,
                               @PathVariable("contractId") Long contractId) {
        String result = couponCommandService.createIssuedCoupon(couponId, contractId);
        return result;
    }

    @PutMapping("/log/{contractId}")
    public ResponseEntity<?> updateIssuedCoupon(@PathVariable("contractId") Long contractId) {
        couponCommandService.updateIssuedCoupon(contractId);
        return ResponseEntity.ok().build();
    }
}
