package com.devoops.rentalbrain.customer.customersupport.command.controller;

import com.devoops.rentalbrain.customer.customersupport.command.dto.FeedbackDTO;
import com.devoops.rentalbrain.customer.customersupport.command.service.CustomersupportCommandFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
public class CustomersupportCommandFeedbackController {

    private final CustomersupportCommandFeedbackService commandService;

    @PostMapping
    public ResponseEntity<Long> register(@RequestBody FeedbackDTO dto) {
        return ResponseEntity.ok(commandService.registerFeedback(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody FeedbackDTO dto) {
        commandService.updateFeedback(id, dto);
        return ResponseEntity.ok("피드백이 수정되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commandService.deleteFeedback(id);
        return ResponseEntity.ok("피드백이 삭제되었습니다.");
    }
}