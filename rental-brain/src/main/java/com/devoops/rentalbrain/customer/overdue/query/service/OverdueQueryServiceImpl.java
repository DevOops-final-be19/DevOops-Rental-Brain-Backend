package com.devoops.rentalbrain.customer.overdue.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.overdue.query.dto.*;
import com.devoops.rentalbrain.customer.overdue.query.mapper.OverdueQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OverdueQueryServiceImpl implements OverdueQueryService {

    private final OverdueQueryMapper overdueQueryMapper;

    @Override
    public PageResponseDTO<PayOverdueListDTO> getPayOverdueList(Criteria criteria,
                                                                String status) {

        // 1. 목록 조회
        List<PayOverdueListDTO> list =
                overdueQueryMapper.findPayOverdueList(criteria, status);

        // 2. 전체 개수
        long totalCount =
                overdueQueryMapper.countPayOverdueList(criteria, status);

        // 3. 페이징 버튼 계산
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        // 4. 응답 DTO 조립
        return new PageResponseDTO<>(list, totalCount, paging);
    }

    @Override
    public PayOverdueDetailDTO getPayOverdueDetail(Long overdueId) {

        PayOverdueDetailDTO dto =
                overdueQueryMapper.findPayOverdueDetail(overdueId);

        if (dto == null) {
            throw new IllegalArgumentException("수납 연체 정보 없음");
        }

        return dto;
    }

    @Override
    public PageResponseDTO<ItemOverdueListDTO> getItemOverdueList(Criteria criteria,
                                                                  String status) {

        List<ItemOverdueListDTO> list =
                overdueQueryMapper.findItemOverdueList(criteria, status);

        long totalCount =
                overdueQueryMapper.countItemOverdueList(criteria, status);

        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        return new PageResponseDTO<>(list, totalCount, paging);
    }

    @Override
    public Map<String, Object> getItemOverdueDetail(Long overdueId) {

        ItemOverdueDetailDTO overdue =
                overdueQueryMapper.findItemOverdueDetail(overdueId);

        if (overdue == null) {
            throw new IllegalArgumentException("제품 연체 정보 없음");
        }

        List<OverdueItemDTO> items =
                overdueQueryMapper.findOverdueItemsByContractId(overdue.getContractId());

        Map<String, Object> result = new HashMap<>();
        result.put("detail", overdue);
        result.put("items", items);

        return result;
    }
}

