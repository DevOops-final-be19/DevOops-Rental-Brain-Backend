package com.devoops.rentalbrain.customer.customersupport.query.controller;

import com.devoops.rentalbrain.common.Pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import com.devoops.rentalbrain.customer.customersupport.query.service.CustomersupportQueryCustomersupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer-supports")
public class CustomersupportQueryCustomersupportController {

    private final CustomersupportQueryCustomersupportService queryService;

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

    @GetMapping("/{id}")
    public ResponseEntity<CustomersupportDTO> detail(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getSupportDetail(id));
    }
}