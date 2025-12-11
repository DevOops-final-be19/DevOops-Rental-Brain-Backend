package com.devoops.rentalbrain.product.productlist.query.service;

import com.devoops.rentalbrain.product.productlist.query.dto.EachItemDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemKpiDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemNameDTO;
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
    public List<ItemNameDTO> readItemsGroupByName() {
        log.info("서비스 계층 실행됨..");
        List<ItemNameDTO> itemNameList = itemMapper.selectItemsByName();

        return itemNameList;
    }

    @Override
    public List<ItemNameDTO> searchItemsByName(String keyword) {
        List<ItemNameDTO> itemNameList = itemMapper.searchItemByName(keyword);

        return itemNameList;
    }

    @Override
    public ItemKpiDTO countItems() {
        ItemKpiDTO result = itemMapper.countItems();
        return result;
    }
}
