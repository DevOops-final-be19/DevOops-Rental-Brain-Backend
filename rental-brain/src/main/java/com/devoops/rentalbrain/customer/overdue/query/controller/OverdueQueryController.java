package com.devoops.rentalbrain.customer.overdue.query.controller;

import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueListDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueListDTO;
import com.devoops.rentalbrain.customer.overdue.query.service.OverdueQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers/overdues")
@RequiredArgsConstructor
public class OverdueQueryController {

    private final OverdueQueryService overdueQueryService;

    /* 수납 연체 목록 */
    @GetMapping("/pay")
    public List<PayOverdueListDTO> getPayOverdueList() {
        return overdueQueryService.getPayOverdueList();
    }

    /* 수납 연체 상세 */
    @GetMapping("/pay/{id}")
    public PayOverdueDetailDTO getPayOverdueDetail(@PathVariable Long id) {
        return overdueQueryService.getPayOverdueDetail(id);
    }

//    @GetMapping("/item")
//    public List<ItemOverdueListDTO> getItemOverdues() {
//        return service.getItemOverdues();
//    }
}
