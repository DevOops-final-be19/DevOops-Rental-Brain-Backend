package com.devoops.rentalbrain.customer.customersegmenthistory.command.service;

import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandCreateDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandResponseDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandUpdateDTO;

public interface  HistoryCommandService {
    HistoryCommandResponseDTO create(HistoryCommandCreateDTO historyCommandCreateDTO);
    HistoryCommandResponseDTO update(Long id, HistoryCommandUpdateDTO historyCommandUpdateDTO);
    void delete(Long id);
}
