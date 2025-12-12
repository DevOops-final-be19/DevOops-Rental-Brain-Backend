package com.devoops.rentalbrain.customer.customersupport.command.controller;

import com.devoops.rentalbrain.customer.customersupport.command.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.command.service.CustomersupportCommandCustomersupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer-supports")
public class CustomersupportCommandCustomersupportController {

    private final CustomersupportCommandCustomersupportService commandService;

    @PostMapping("/insertSupport")
    public ResponseEntity<Long> register(@RequestBody CustomersupportDTO dto) {
        return ResponseEntity.ok(commandService.registerSupport(dto));
    }

    @PutMapping("/updateSupport/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CustomersupportDTO dto) {
        commandService.updateSupport(id, dto);
        return ResponseEntity.ok("문의가 수정되었습니다.");
    }

    @DeleteMapping("/deleteSupport/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commandService.deleteSupport(id);
        return ResponseEntity.ok("문의가 삭제되었습니다.");
    }
}