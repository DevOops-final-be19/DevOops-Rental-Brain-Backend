package com.devoops.rentalbrain.product.productlist.command.repository;

import com.devoops.rentalbrain.product.productlist.command.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    ItemCategory findAllByName(String categoryName);
}
