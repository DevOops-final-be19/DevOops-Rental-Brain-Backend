package com.devoops.rentalbrain.customer.overdue.command.controller;

import com.devoops.rentalbrain.customer.overdue.command.dto.PayOverdueCommandDTO;
import com.devoops.rentalbrain.customer.overdue.command.service.OverdueCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers/overdues")
public class OverdueCommandController {

    private final OverdueCommandService overdueCommandService;

    /* 수납 연체 처리 */
    @PutMapping("/pay/{id}")
    public void updatePayOverdue(
            @PathVariable Long id,
            @RequestBody PayOverdueCommandDTO dto
    ) {
        overdueCommandService.updatePayOverdue(id, dto);
    }
}

