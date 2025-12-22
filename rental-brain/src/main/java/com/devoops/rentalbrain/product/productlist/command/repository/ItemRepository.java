package com.devoops.rentalbrain.product.productlist.command.repository;

import com.devoops.rentalbrain.product.productlist.command.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = """
        SELECT id
        FROM item
        WHERE name = :name
          AND status = 'P'
        ORDER BY id ASC
        LIMIT :quantity
    """, nativeQuery = true)
    List<Long> findRentableItemIdsByName(
            @Param("name") String name,
            @Param("quantity") int quantity
    );

    @Modifying
    @Query(value = """
        UPDATE item
        SET status = 'S'
        WHERE id IN (:ids)
    """, nativeQuery = true)
    int updateItemStatusToRented(@Param("ids") List<Long> ids);

    List<Item> findByName(String name);

}
