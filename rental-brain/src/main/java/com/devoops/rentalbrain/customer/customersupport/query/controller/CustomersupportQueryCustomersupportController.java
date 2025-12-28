package com.devoops.rentalbrain.customer.customersupport.query.controller;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import com.devoops.rentalbrain.customer.customersupport.query.service.CustomersupportQueryCustomersupportService;
import com.devoops.rentalbrain.employee.query.dto.InChargeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "고객 문의 조회(Query)",
        description = "문의 목록 조회, 상세 조회 등 고객 문의 관련 조회(Query) API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/customersupports")
public class CustomersupportQueryCustomersupportController {

    private final CustomersupportQueryCustomersupportService queryService;

    @Operation(summary = "문의 목록 조회", description = "검색어, 카테고리, 상태, 정렬 조건을 포함하여 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<CustomersupportDTO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        CustomersupportSearchDTO criteria = new CustomersupportSearchDTO(page, size);
        criteria.setKeyword(keyword);
        criteria.setStatus(status);
        criteria.setCategory(category);
        criteria.setSortBy(sortBy);
        criteria.setSortOrder(sortOrder);

        return ResponseEntity.ok(queryService.getSupportList(criteria));
    }

    @Operation(summary = "문의 상세 조회", description = "특정 문의 ID에 대한 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문의"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomersupportDTO> detail(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getSupportDetail(id));
    }

    @Operation(summary = "문의 KPI 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/kpi")
    public ResponseEntity<CustomersupportKpiDTO> getKpi() {
        return ResponseEntity.ok(queryService.getSupportKpi());
    }

    @Operation(summary = "담당자 목록 조회", description = "문의 등록 시 선택할 담당자(직원) 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/in-charge")
    public ResponseEntity<List<InChargeDTO>> getInChargeList() {
        return ResponseEntity.ok(queryService.getInChargeList());
    }
}