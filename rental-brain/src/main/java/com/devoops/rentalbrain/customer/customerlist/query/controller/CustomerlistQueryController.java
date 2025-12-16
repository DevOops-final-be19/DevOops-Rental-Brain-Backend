package com.devoops.rentalbrain.customer.customerlist.query.controller;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.devoops.rentalbrain.customer.customerlist.query.service.CustomerlistQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "고객 조회(Query)",
        description = "고객 목록 조회, 상세 조회 등 고객 관련 조회(Query) API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerlistQueryController {

    private final CustomerlistQueryService queryService;

    @Operation(summary = "고객 목록 조회", description = "이름, 이메일 등의 검색 조건과 페이징을 적용하여 고객 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<CustomerDTO>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        CustomerlistSearchDTO criteria = new CustomerlistSearchDTO(page, size);
        criteria.setName(name);
        criteria.setEmail(email);

        return ResponseEntity.ok(queryService.getCustomerListWithPaging(criteria));
    }

    @Operation(summary = "고객 상세 조회", description = "고객 정보 및 관련 내역(문의, 견적, 계약, AS 등)을 모두 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 고객"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailResponseDTO> detail(@PathVariable Long id) { // 반환 타입 변경
        return ResponseEntity.ok(queryService.getCustomerDetail(id));
    }
}