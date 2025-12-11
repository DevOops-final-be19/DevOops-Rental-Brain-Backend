package com.devoops.rentalbrain.product.productlist.query.mapper;

import com.devoops.rentalbrain.product.productlist.query.dto.EachItemDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemKpiDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemNameDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {
    List<EachItemDTO> selectAllItems(String itemName);

    List<ItemNameDTO> selectItemsByName();

    List<ItemNameDTO> searchItemByName(String keyword);

    ItemKpiDTO countItems();
}
