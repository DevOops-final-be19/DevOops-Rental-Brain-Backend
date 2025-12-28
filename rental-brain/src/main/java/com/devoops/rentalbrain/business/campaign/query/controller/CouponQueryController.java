package com.devoops.rentalbrain.business.campaign.query.controller;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.CouponWithContractDTO;
import com.devoops.rentalbrain.business.campaign.query.service.CouponQueryService;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "쿠폰 목록 조회",
            description = "전체 쿠폰 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/read-list")
    public ResponseEntity<PageResponseDTO<CouponDTO>> readCouponList(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);
        PageResponseDTO<CouponDTO> couponList = couponQueryService.readCouponList(criteria);
        return ResponseEntity.ok().body(couponList);
    }

    @Operation(
            summary = "쿠폰 검색",
            description = "코드나 이름으로 쿠폰을 검색합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageResponseDTO<CouponDTO>> searchCoupon(@PathVariable String keyword,
                                                                   @RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);
        PageResponseDTO<CouponDTO> couponList = couponQueryService.searchCoupon(keyword, criteria);
        return ResponseEntity.ok().body(couponList);
    }

    @Operation(
            summary = "쿠폰 목록 유형 필터링",
            description = "전체 쿠폰 목록을 유형으로 필터링합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/filter-type/{type}")
    public ResponseEntity<PageResponseDTO<CouponDTO>> filteringCouponByType(@PathVariable String type,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);
        PageResponseDTO<CouponDTO> couponList = couponQueryService.filterCouponByType(type, criteria);
        return ResponseEntity.ok().body(couponList);
    }

    @Operation(
            summary = "쿠폰 목록 상태 필터링",
            description = "전체 쿠폰 목록을 상태로 필터링합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/filter-status/{status}")
    public ResponseEntity<PageResponseDTO<CouponDTO>> filteringCouponByStatus(@PathVariable String status,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);
        PageResponseDTO<CouponDTO> couponList = couponQueryService.filterCouponByStatus(status, criteria);
        return ResponseEntity.ok().body(couponList);
    }

    @Operation(
            summary = "계약 시 쿠폰 조회",
            description = "계약 시 세그먼트에 따라 쿠폰을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/use-contract/{segment}")
    public ResponseEntity<List<CouponWithContractDTO>> useContract(@PathVariable Integer segment) {
        List<CouponWithContractDTO> couponList = couponQueryService.useContractCoupon(segment);
        return ResponseEntity.ok().body(couponList);
    }

    @Operation(
            summary = "쿠폰 상세 조회",
            description = "각 쿠폰별 정보를 상세 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/read-detail/{couCode}")
    public ResponseEntity<CouponDTO> readDetail(@PathVariable String couCode) {
        CouponDTO coupon = couponQueryService.readDetailCoupon(couCode);
        return ResponseEntity.ok().body(coupon);
    }
}
