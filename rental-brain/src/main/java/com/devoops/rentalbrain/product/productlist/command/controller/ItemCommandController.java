package com.devoops.rentalbrain.product.productlist.command.controller;

import com.devoops.rentalbrain.product.productlist.command.dto.ItemDTO;
import com.devoops.rentalbrain.product.productlist.command.dto.ModifyItemDTO;
import com.devoops.rentalbrain.product.productlist.command.service.ItemCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemCommandController {
    private final ItemCommandService itemCommandService;

    @Autowired
    public ItemCommandController(ItemCommandService itemCommandService) {
        this.itemCommandService = itemCommandService;
    }

    @PostMapping("/insert")
    public void insertNewItem(@RequestBody ItemDTO itemDTO) {
        itemCommandService.insertNewItem(itemDTO);
    }

    @PutMapping("/update/{itemId}")
    public String updateItem(@PathVariable int itemId, @RequestBody ModifyItemDTO itemDTO) {
        String result = itemCommandService.updateItem(itemId, itemDTO);

        return result;
    }

    @DeleteMapping("/delete/{itemId}")
    public String deleteItem(@PathVariable int itemId) {
        String result = itemCommandService.deleteItem(itemId);

        return result;
    }
}
