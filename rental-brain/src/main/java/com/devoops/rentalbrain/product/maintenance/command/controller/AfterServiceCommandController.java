package com.devoops.rentalbrain.product.maintenance.command.controller;

import com.devoops.rentalbrain.product.maintenance.command.dto.*;
import com.devoops.rentalbrain.product.maintenance.command.service.AfterServiceCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance/as")
@RequiredArgsConstructor
public class AfterServiceCommandController {

    private final AfterServiceCommandService service;

    @PostMapping
    public Long create(@RequestBody AfterServiceCreateRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody AfterServiceUpdateRequest req) {
        service.update(id, req);
    }
}