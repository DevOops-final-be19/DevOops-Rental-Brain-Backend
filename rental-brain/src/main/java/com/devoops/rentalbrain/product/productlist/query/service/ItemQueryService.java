package com.devoops.rentalbrain.product.productlist.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.*;

import java.util.List;

public interface ItemQueryService {
    List<EachItemDTO> readAllItems(String itemName);

    PageResponseDTO<ItemNameDTO> readItemsGroupByName(Criteria criteria);

    PageResponseDTO<ItemNameDTO> searchItemsByName(String keyword, Criteria criteria);

    ItemKpiDTO countItems();

    PageResponseDTO<ItemNameDTO> filteringItemsByCategory(String categoryName, Criteria criteria);

    List<ItemCategoryDTO> readCategory();

    EachItemKpiDTO countEachItemKpi(String itemName);
}
