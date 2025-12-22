package com.devoops.rentalbrain.customer.overdue.query.service;

import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueListDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueListDTO;

import java.util.List;

public interface OverdueQueryService {

    List<PayOverdueListDTO> getPayOverdueList();
    PayOverdueDetailDTO getPayOverdueDetail(Long overdueId);

//    List<ItemOverdueListDTO> getItemOverdues();
//    ItemOverdueDetailDTO getItemOverdue(Long id);

}
