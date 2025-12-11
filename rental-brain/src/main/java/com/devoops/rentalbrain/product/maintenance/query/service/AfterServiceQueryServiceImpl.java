package com.devoops.rentalbrain.product.maintenance.query.service;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceResponse;
import com.devoops.rentalbrain.product.maintenance.query.mapper.AfterServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AfterServiceQueryServiceImpl implements AfterServiceQueryService {

    private final AfterServiceMapper mapper;

    @Override
    public List<AfterServiceResponse> findAll() {
        return mapper.findAll();
    }
}
