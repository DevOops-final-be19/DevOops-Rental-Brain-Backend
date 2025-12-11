package com.devoops.rentalbrain.product.productlist.query.service;

import com.devoops.rentalbrain.product.productlist.query.dto.EachItemDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemKpiDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemNameDTO;

import java.util.List;

public interface ItemQueryService {
    List<EachItemDTO> readAllItems(String itemName);

    List<ItemNameDTO> readItemsGroupByName();

    List<ItemNameDTO> searchItemsByName(String keyword);

    ItemKpiDTO countItems();
}
