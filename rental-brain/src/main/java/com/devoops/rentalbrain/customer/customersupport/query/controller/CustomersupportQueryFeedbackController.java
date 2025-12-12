package com.devoops.rentalbrain.customer.customersupport.query.controller;

import com.devoops.rentalbrain.common.Pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import com.devoops.rentalbrain.customer.customersupport.query.service.CustomersupportQueryFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
public class CustomersupportQueryFeedbackController {

    private final CustomersupportQueryFeedbackService queryService;

    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<FeedbackDTO>> list(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        FeedbackSearchDTO criteria = new FeedbackSearchDTO(page, size);
        criteria.setTitle(title);

        return ResponseEntity.ok(queryService.getFeedbackList(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> detail(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getFeedbackDetail(id));
    }
}