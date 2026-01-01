package com.devoops.rentalbrain.business.contract.command.controller;

import com.devoops.rentalbrain.business.contract.command.dto.PaymentDetailRequestDTO;
import com.devoops.rentalbrain.business.contract.command.service.PaymentDetailCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-details")
@Tag(name = "결제 관리(command)", description = "계약 결제 내역 처리 API")
public class PaymentDetailCommandController {

    private final PaymentDetailCommandService paymentDetailCommandService;

    @Autowired
    public PaymentDetailCommandController(PaymentDetailCommandService paymentDetailCommandService) {
        this.paymentDetailCommandService = paymentDetailCommandService;
    }

    @Operation(
            summary = "결제 완료 처리",
            description = """
                    특정 결제 내역을 결제 완료(C) 상태로 변경합니다.
                    
                    - 이미 완료된 결제는 처리할 수 없습니다.
                    - 결제 완료 시 계약에 연결된 아이템의 월 매출이 누적됩니다.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 완료 처리 성공"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "결제 내역이 존재하지 않음",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "이미 완료된 결제",
                            content = @Content
                    )
            }
    )
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completePayment(
            @PathVariable Long id,
            @RequestBody PaymentDetailRequestDTO request
    ) {
        paymentDetailCommandService.completePayment(id, request);
        return ResponseEntity.ok().build();
    }

}
