package com.devoops.rentalbrain.product.maintenance.query.service;

import com.devoops.rentalbrain.product.maintenance.query.dto.RentalItemForAsDTO;

import java.util.List;

public interface AfterServiceItemQueryService {

    List<RentalItemForAsDTO> getRentalItemsByCustomer(Long customerId);
}