package com.devoops.rentalbrain.product.productlist.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.product.productlist.query.dto.*;
import com.devoops.rentalbrain.product.productlist.query.mapper.ItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ItemQueryServiceImpl implements ItemQueryService {
    final private ItemMapper itemMapper;

    @Autowired
    public ItemQueryServiceImpl(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }


    @Override
    public List<EachItemDTO> readAllItems(String itemName) {
        List<EachItemDTO> itemsList = itemMapper.selectAllItems(itemName);

        return itemsList;
    }

    @Override
    public PageResponseDTO<ItemNameDTO> readItemsGroupByName(Criteria criteria) {
        log.info("서비스 계층 실행됨..");
        // 1) 데이터 목록 조회
        List<ItemNameDTO> itemNameList = itemMapper.selectItemsByName(criteria.getOffset(),
                criteria.getAmount());

        // 2) 전체 건수 조회
        long totalCount = itemMapper.countItemsList();


        // 3) 페이지 버튼 정보 계산 (MyBatis용 유틸 사용)
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        // 4) 공통 페이지 응답으로 감싸서 반환
        return new PageResponseDTO<>(itemNameList, totalCount, paging);
    }

    @Override
    public PageResponseDTO<ItemNameDTO> searchItemsByName(String keyword, Criteria criteria) {
        List<ItemNameDTO> itemNameList = itemMapper.searchItemByName(keyword, criteria.getOffset(),
                criteria.getAmount());

        long totalCount = itemMapper.countSearchItemsList(keyword);

        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        return new PageResponseDTO<>(itemNameList, totalCount, paging);
    }

    @Override
    public ItemKpiDTO countItems() {
        ItemKpiDTO result = itemMapper.countItems();
        return result;
    }

    @Override
    public PageResponseDTO<ItemNameDTO> filteringItemsByCategory(String categoryName, Criteria criteria) {
        List<ItemNameDTO> itemNameList = itemMapper.filteringItemsByCategory(categoryName, criteria.getOffset(),
                criteria.getAmount());

        long totalCount = itemMapper.countFilteringItemsList(categoryName);

        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        return new PageResponseDTO<>(itemNameList, totalCount, paging);
    }

    @Override
    public List<ItemCategoryDTO> readCategory() {
        List<ItemCategoryDTO> categoryList = itemMapper.selectCategory();
        return categoryList;
    }

    @Override
    public EachItemKpiDTO countEachItemKpi(String itemName) {
        EachItemKpiDTO kpiDTO = itemMapper.countEachItemKpi(itemName);
        return kpiDTO;
    }
}
