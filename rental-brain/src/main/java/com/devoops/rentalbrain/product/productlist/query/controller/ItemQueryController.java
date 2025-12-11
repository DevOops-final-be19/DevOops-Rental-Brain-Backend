package com.devoops.rentalbrain.product.productlist.query.controller;

import com.devoops.rentalbrain.product.productlist.query.dto.EachItemDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemKpiDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemNameDTO;
import com.devoops.rentalbrain.product.productlist.query.service.ItemQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item")
@Slf4j
public class ItemQueryController {
    final private ItemQueryService itemQueryService;

    @Autowired
    public ItemQueryController(ItemQueryService itemQueryService) {
        this.itemQueryService = itemQueryService;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/read-all/{itemName}")
    public ResponseEntity<List<EachItemDTO>> readAllItems(@PathVariable String itemName) {
        List<EachItemDTO> itemsList = itemQueryService.readAllItems(itemName);
        return ResponseEntity.ok().body(itemsList);
    }

    @GetMapping("/read-groupby-name")
    public ResponseEntity<List<ItemNameDTO>> readItemsGroupByName() {
        log.info("컨트롤러 실행됨..");
        List<ItemNameDTO> itemNameList = itemQueryService.readItemsGroupByName();
        return ResponseEntity.ok().body(itemNameList);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ItemNameDTO>> searchItems(@PathVariable String keyword) {
        List<ItemNameDTO> itemNameList = itemQueryService.searchItemsByName(keyword);

        return ResponseEntity.ok().body(itemNameList);
    }

    @GetMapping("/kpi-count")
    public ItemKpiDTO kpiCount() {
        ItemKpiDTO result = itemQueryService.countItems();
        return result;
    }
}
