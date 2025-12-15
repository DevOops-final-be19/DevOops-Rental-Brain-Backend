package com.devoops.rentalbrain.customer.customersupport.query.controller;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import com.devoops.rentalbrain.customer.customersupport.query.service.CustomersupportQueryCustomersupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "고객 문의 조회(Query)",
        description = "문의 목록 조회, 상세 조회 등 고객 문의 관련 조회(Query) API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/customersupports")
public class CustomersupportQueryCustomersupportController {

    private final CustomersupportQueryCustomersupportService queryService;

    @Operation(summary = "문의 목록 조회", description = "제목, 상태 등의 검색 조건과 페이징을 적용하여 문의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<CustomersupportDTO>> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        CustomersupportSearchDTO criteria = new CustomersupportSearchDTO(page, size);
        criteria.setTitle(title);
        criteria.setStatus(status);

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
}