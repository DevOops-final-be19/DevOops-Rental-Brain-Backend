package com.devoops.rentalbrain.business.contract.command.controller;

import com.devoops.rentalbrain.business.contract.command.dto.PaymentDetailRequestDTO;
import com.devoops.rentalbrain.business.contract.command.service.PaymentDetailCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-details")
public class PaymentDetailCommandController {

    private final PaymentDetailCommandService paymentDetailCommandService;

    @Autowired
    public PaymentDetailCommandController(PaymentDetailCommandService paymentDetailCommandService) {
        this.paymentDetailCommandService = paymentDetailCommandService;
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completePayment(
            @PathVariable Long id,
            @RequestBody PaymentDetailRequestDTO request
    ) {
        paymentDetailCommandService.completePayment(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/non-payment")
    public ResponseEntity<Void> markAsNonPayment(@PathVariable Long id) {
        paymentDetailCommandService.markAsNonPayment(id);
        return ResponseEntity.noContent().build();
    }
}
