package com.devoops.rentalbrain.product.maintenance.query.service;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceResponse;
import java.util.List;

public interface AfterServiceQueryService {

    List<AfterServiceResponse> findAll();
}
