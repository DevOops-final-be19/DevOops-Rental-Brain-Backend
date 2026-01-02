package com.devoops.rentalbrain.product.productlist.query.controller;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.*;
import com.devoops.rentalbrain.product.productlist.query.service.ItemQueryService;
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
@RequestMapping("/item")
@Slf4j
@Tag(name = "제품 관리(Query)",
        description = "제품 정보 조회 관련 API")
public class ItemQueryController {
    final private ItemQueryService itemQueryService;

    @Autowired
    public ItemQueryController(ItemQueryService itemQueryService) {
        this.itemQueryService = itemQueryService;
    }

    @Operation(
            summary = "헬스 체크",
            description = "서버 상태 확인용 API (인증 불필요)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "서버 정상 동작")
            }
    )
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @Operation(
            summary = "제품 상세 목록 조회",
            description = "제품명이 일치하는 개별 제품들의 상세 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/read-all/{itemName}")
    public ResponseEntity<List<EachItemDTO>> readAllItems(@PathVariable String itemName) {
        List<EachItemDTO> itemsList = itemQueryService.readAllItems(itemName);
        return ResponseEntity.ok().body(itemsList);
    }

    @Operation(
            summary = "제품 목록 조회",
            description = "전체 제품 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/read-groupby-name")
    public ResponseEntity<PageResponseDTO<ItemNameDTO>> readItemsGroupByName(@RequestParam(defaultValue = "1") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {

        // 공용 Criteria 사용 (페이지 정보만 사용)
        Criteria criteria = new Criteria(page, size);

        PageResponseDTO<ItemNameDTO> itemNameList = itemQueryService.readItemsGroupByName(criteria);
        return ResponseEntity.ok().body(itemNameList);
    }

    @Operation(
            summary = "제품 검색",
            description = "제품명으로 제품을 검색합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageResponseDTO<ItemNameDTO>> searchItems(@PathVariable String keyword,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        // 공용 Criteria 사용 (페이지 정보만 사용)
        Criteria criteria = new Criteria(page, size);

        PageResponseDTO<ItemNameDTO> itemNameList = itemQueryService.searchItemsByName(keyword, criteria);

        return ResponseEntity.ok().body(itemNameList);
    }

    @Operation(
            summary = "전체 제품 kpi 카드 조회",
            description = "전체 제품의 kpi 카드에 들어갈 수를 카운트합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/kpi-count")
    public ItemKpiDTO kpiCount() {
        ItemKpiDTO result = itemQueryService.countItems();
        return result;
    }

    @Operation(
            summary = "제품 목록 필터링",
            description = "전체 제품 목록을 제품 카테고리로 필터링합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/filtering/{categoryName}")
    public ResponseEntity<PageResponseDTO<ItemNameDTO>> filteringItemByCategory(@PathVariable String categoryName,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);

        PageResponseDTO<ItemNameDTO> itemNameList = itemQueryService.filteringItemsByCategory(categoryName, criteria);

        return ResponseEntity.ok().body(itemNameList);
    }

    @Operation(
            summary = "제품 카테고리 조회",
            description = "제품 카테고리를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/category")
    public ResponseEntity<List<ItemCategoryDTO>> readCategory() {
        List<ItemCategoryDTO> categoryList = itemQueryService.readCategory();
        return ResponseEntity.ok().body(categoryList);
    }

    @Operation(
            summary = "각 제품별 kpi 카드 조회",
            description = "제품별 kpi 카드에 들어갈 수를 카운트합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/kpi/{itemName}")
    public EachItemKpiDTO readItemKpi(@PathVariable String itemName) {
        EachItemKpiDTO kpiDTO = itemQueryService.countEachItemKpi(itemName);
        return kpiDTO;
    }
}
