package com.devoops.rentalbrain.customer.customersegmenthistory.query.service;

import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.HistoryListQueryDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.HistoryQueryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryQueryService {
    HistoryListQueryDTO getByCustomer(Long customerId);

    HistoryQueryDTO getHistory(Long historyId);
}
