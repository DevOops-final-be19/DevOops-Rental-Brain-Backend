package com.devoops.rentalbrain.customer.overdue.query.mapper;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.customer.overdue.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OverdueQueryMapper {
    // 수납 연체
    List<PayOverdueListDTO> findPayOverdueList(@Param("criteria") Criteria criteria,
                                               @Param("status") String status);
    long countPayOverdueList(@Param("criteria") Criteria criteria,
                             @Param("status") String status);

    PayOverdueDetailDTO findPayOverdueDetail(@Param("overdueId") Long overdueId);

    // 제품 연체
    List<ItemOverdueListDTO> findItemOverdueList(@Param("criteria") Criteria criteria,
                                                 @Param("status") String status);
    long countItemOverdueList(@Param("criteria") Criteria criteria,
                              @Param("status") String status);

    ItemOverdueDetailDTO findItemOverdueDetail(@Param("overdueId") Long overdueId);

    List<OverdueItemDTO> findOverdueItemsByContractId(@Param("contractId") Long contractId);
}
