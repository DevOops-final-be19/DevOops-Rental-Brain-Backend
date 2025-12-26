package com.devoops.rentalbrain.customer.overdue.query.mapper;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueCandidateDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OverdueQueryMapper {

    List<PayOverdueListDTO> findPayOverdueList(
            @Param("criteria") Criteria criteria,
            @Param("status") String status
    );

    long countPayOverdueList(
            @Param("criteria") Criteria criteria,
            @Param("status") String status
    );

    List<ItemOverdueListDTO> findItemOverdueList(
            @Param("criteria") Criteria criteria,
            @Param("status") String status
    );

    long countItemOverdueList(
            @Param("criteria") Criteria criteria,
            @Param("status") String status
    );

    PayOverdueDetailDTO findPayOverdueDetail(Long overdueId);

    ItemOverdueDetailDTO findItemOverdueDetail(Long overdueId);

    List<OverdueItemDTO> findOverdueItemsByContractId(Long contractId);

    List<ItemOverdueCandidateDTO> findItemOverdueCandidates();
}

