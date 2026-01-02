package com.devoops.rentalbrain.product.productlist.query.mapper;

import com.devoops.rentalbrain.product.productlist.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper {
    List<EachItemDTO> selectAllItems(String itemName);

    List<ItemNameDTO> selectItemsByName(@Param("offset") int offset,
                                        @Param("limit") int limit);

    List<ItemNameDTO> searchItemByName(String keyword,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    ItemKpiDTO countItems();

    long countItemsList();

    List<ItemNameDTO> filteringItemsByCategory(String categoryName, @Param("offset") int offset,
                                               @Param("limit") int limit);

    long countSearchItemsList(String keyword);

    long countFilteringItemsList(String categoryName);

    List<ItemCategoryDTO> selectCategory();

    EachItemKpiDTO countEachItemKpi(String itemName);
}
