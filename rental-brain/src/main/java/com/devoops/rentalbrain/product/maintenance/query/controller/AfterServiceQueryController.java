package com.devoops.rentalbrain.product.maintenance.query.controller;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceResponse;
import com.devoops.rentalbrain.product.maintenance.query.service.AfterServiceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance/as")
@RequiredArgsConstructor
public class AfterServiceQueryController {

    private final AfterServiceQueryService service;

    @GetMapping
    public List<AfterServiceResponse> findAll() {
        return service.findAll();
    }
}
