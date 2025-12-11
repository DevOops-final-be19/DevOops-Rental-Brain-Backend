package com.devoops.rentalbrain.customer.customerlist.query.controller;

import com.devoops.rentalbrain.common.Pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.common.CustomerDto;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.devoops.rentalbrain.customer.customerlist.query.service.CustomerlistQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@Slf4j
public class CustomerlistQueryController {

    private final CustomerlistQueryService queryService;

    // 고객 목록 조회 (페이징 + 검색)
    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<CustomerDto>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        CustomerlistSearchDTO criteria = new CustomerlistSearchDTO(page, size);
        criteria.setName(name);
        criteria.setEmail(email);

        return ResponseEntity.ok(queryService.getCustomerListWithPaging(criteria));
    }

    // 고객 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getCustomerDetail(id));
    }
}