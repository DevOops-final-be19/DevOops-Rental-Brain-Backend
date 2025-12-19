package com.devoops.rentalbrain.product.maintenance.query.service;

import com.devoops.rentalbrain.product.maintenance.query.dto.RentalItemForAsDTO;
import com.devoops.rentalbrain.product.maintenance.query.mapper.AfterServiceItemQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AfterServiceItemQueryServiceImpl implements AfterServiceItemQueryService {

        private final AfterServiceItemQueryMapper afterServiceItemQueryMapper;

        @Override
        public List<RentalItemForAsDTO> getRentalItemsByCustomer(Long customerId) {
            return afterServiceItemQueryMapper.selectRentalItemsByCustomer(customerId);
        }
}
