package com.devoops.rentalbrain.customer.overdue.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueListDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueListDTO;

public interface OverdueQueryService {

    PageResponseDTO<PayOverdueListDTO> getPayOverdueList(Criteria criteria, String status);
    PayOverdueDetailDTO getPayOverdueDetail(Long overdueId);

    PageResponseDTO<ItemOverdueListDTO> getItemOverdueList(Criteria criteria, String status);
    java.util.Map<String, Object> getItemOverdueDetail(Long overdueId);

}
