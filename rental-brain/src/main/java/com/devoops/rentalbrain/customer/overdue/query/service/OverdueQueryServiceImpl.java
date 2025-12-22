package com.devoops.rentalbrain.customer.overdue.query.service;

import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueListDTO;
import com.devoops.rentalbrain.customer.overdue.query.mapper.OverdueQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OverdueQueryServiceImpl implements OverdueQueryService {

    private final OverdueQueryMapper overdueQueryMapper;

    @Override
    public List<PayOverdueListDTO> getPayOverdueList() {
        return overdueQueryMapper.findPayOverdueList();
    }

    @Override
    public PayOverdueDetailDTO getPayOverdueDetail(Long overdueId) {
        return overdueQueryMapper.findPayOverdueDetail(overdueId);
    }
}

