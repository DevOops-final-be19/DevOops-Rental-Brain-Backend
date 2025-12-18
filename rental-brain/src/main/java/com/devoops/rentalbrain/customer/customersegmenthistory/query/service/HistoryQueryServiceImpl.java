package com.devoops.rentalbrain.customer.customersegmenthistory.query.service;

import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.HistoryListQueryDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.HistoryQueryDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.mapper.HistoryQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HistoryQueryServiceImpl implements HistoryQueryService {

    private final HistoryQueryMapper historyQueryMapper;

    @Autowired
    public HistoryQueryServiceImpl(HistoryQueryMapper historyQueryMapper) {
        this.historyQueryMapper = historyQueryMapper;
    }

    // 세그먼트 번경 이력 list 조회
    @Override
    public HistoryListQueryDTO getByCustomer(Long customerId) {

        List<HistoryQueryDTO> histories = historyQueryMapper.getByCustomer(customerId);

        String customerName = histories.isEmpty()
                ? null : histories.get(0).getCustomerName(); // 만약 customerName을 내려주고 있다면

        log.info("세그먼트 변경 이력 조회 - customerId: {}, count: {}", customerId, histories.size());

        return new HistoryListQueryDTO(
                customerId,
                customerName,
                histories
        );
    }

    // 세그먼트 변경 이력 detail 조회
    @Override
    public HistoryQueryDTO getHistory(Long historyId) {
        HistoryQueryDTO detail = historyQueryMapper.getHistory(historyId);

        return detail;
    }
}

