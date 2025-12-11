package com.devoops.rentalbrain.product.productlist.query.controller;

import com.devoops.rentalbrain.product.productlist.query.dto.EachItemDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemKpiDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemNameDTO;
import com.devoops.rentalbrain.product.productlist.query.service.ItemQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<ItemNameDTO>> readItemsGroupByName(@RequestParam(defaultValue = "1") int page,    // 페이징
                                                                  @RequestParam(defaultValue = "10") int size) {

        // 공용 Criteria 사용 (페이지 정보만 사용)
//        Criteria criteria = new Criteria(page, size);
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
