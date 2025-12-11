package com.devoops.rentalbrain.product.maintenance.command.service;

import com.devoops.rentalbrain.product.maintenance.command.dto.AfterServiceCreateRequest;
import com.devoops.rentalbrain.product.maintenance.command.dto.AfterServiceUpdateRequest;

public interface AfterServiceCommandService {

    Long create(AfterServiceCreateRequest req);

    void update(Long id, AfterServiceUpdateRequest req);
}