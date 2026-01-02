package com.devoops.rentalbrain.customer.customersegmenthistory.query.mapper;

import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.HistoryQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HistoryQueryMapper {
    List<HistoryQueryDTO> getByCustomer(Long customerId);

    HistoryQueryDTO getHistory(Long historyId);
}
