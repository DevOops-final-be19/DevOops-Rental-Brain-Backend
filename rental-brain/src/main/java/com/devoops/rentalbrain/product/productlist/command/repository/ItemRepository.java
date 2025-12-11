package com.devoops.rentalbrain.product.productlist.command.repository;

import com.devoops.rentalbrain.product.productlist.command.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
