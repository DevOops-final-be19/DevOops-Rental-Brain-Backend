package com.devoops.rentalbrain.customer.customerlist.command.controller;

import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerCommandDto;
import com.devoops.rentalbrain.customer.customerlist.command.service.CustomerCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@Slf4j
public class CustomerCommandController {

    private final CustomerCommandService commandService;

    @PostMapping
    public ResponseEntity<Long> register(@RequestBody CustomerCommandDto dto) {
        Long savedId = commandService.registerCustomer(dto);
        log.info("고객 등록 완료 ID: {}", savedId);
        return ResponseEntity.ok(savedId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CustomerCommandDto dto) {
        commandService.updateCustomer(id, dto);
        log.info("고객 수정 완료 ID: {}", id);
        return ResponseEntity.ok("고객 정보가 수정되었습니다.");
    }
}