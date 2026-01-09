package com.devoops.rentalbrain.product.maintenance.command.service;

import com.devoops.rentalbrain.product.maintenance.command.dto.AfterServiceCreateRequest;
import com.devoops.rentalbrain.product.maintenance.command.dto.AfterServiceUpdateRequest;

public interface AfterServiceCommandService {

    void create(AfterServiceCreateRequest request);
    void update(Long asId, AfterServiceUpdateRequest request);
    void delete(Long asId);

    void autoCompleteAndCreateNext();
}