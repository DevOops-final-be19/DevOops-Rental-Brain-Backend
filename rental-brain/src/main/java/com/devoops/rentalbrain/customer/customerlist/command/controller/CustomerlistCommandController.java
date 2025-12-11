package com.devoops.rentalbrain.customer.customerlist.command.controller;

import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;
import com.devoops.rentalbrain.customer.customerlist.command.service.CustomerlistCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@Slf4j
public class CustomerlistCommandController {

    private final CustomerlistCommandService commandService;

    // 고객 등록
    @PostMapping("/insertCustomer")
    public ResponseEntity<Long> register(@RequestBody CustomerlistCommandDTO dto) {
        Long savedId = commandService.registerCustomer(dto);
        log.info("고객 등록 완료 ID: {}", savedId);
        return ResponseEntity.ok(savedId);
    }

    // 고객 수정
    @PutMapping("/updateCustomer/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CustomerlistCommandDTO dto) {
        commandService.updateCustomer(id, dto);
        log.info("고객 수정 완료 ID: {}", id);
        return ResponseEntity.ok("고객 정보가 수정되었습니다.");
    }

    // 고객 삭제 (Soft Delete)
    @DeleteMapping("/deleteCustomer/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commandService.deleteCustomer(id);
        log.info("고객 삭제(Soft Delete) 완료 ID: {}", id);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    // 고객 복구 (Restore)
    @PutMapping("/restoreCustomer/{id}")
    public ResponseEntity<String> restore(@PathVariable Long id) {
        commandService.restoreCustomer(id);
        log.info("고객 복구 요청 완료 ID: {}", id);
        return ResponseEntity.ok("고객이 정상적으로 복구되었습니다.");
    }
}