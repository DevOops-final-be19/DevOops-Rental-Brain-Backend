package com.devoops.rentalbrain.product.productlist.command.service;

import com.devoops.rentalbrain.product.productlist.command.dto.ItemDTO;
import com.devoops.rentalbrain.product.productlist.command.dto.ModifyItemDTO;

public interface ItemCommandService {
    void insertNewItem(ItemDTO itemDTO);

    String updateItem(int itemId, ModifyItemDTO itemDTO);

    String deleteItem(int itemId);
}
