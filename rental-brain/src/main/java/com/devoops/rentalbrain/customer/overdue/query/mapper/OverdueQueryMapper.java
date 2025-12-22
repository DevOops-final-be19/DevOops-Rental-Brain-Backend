package com.devoops.rentalbrain.customer.overdue.query.mapper;

import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueListDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OverdueQueryMapper {
    // 수납 연체
    List<PayOverdueListDTO> findPayOverdueList();
    PayOverdueDetailDTO findPayOverdueDetail(@Param("id") Long id);

    // 제품 연체
    List<ItemOverdueListDTO> findItemOverdueList();
    ItemOverdueDetailDTO findItemOverdueDetail(@Param("id") Long id);
}
