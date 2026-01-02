package com.devoops.rentalbrain.product.productlist.command.repository;

import com.devoops.rentalbrain.product.productlist.command.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query(value = """
        SELECT i.id
        FROM item i
        WHERE i.name = :name
          AND i.status = 'P'
        ORDER BY i.id ASC
        LIMIT :quantity
    """, nativeQuery = true)
    List<Long> findRentableItemIdsForContract(
            @Param("name") String name,
            @Param("quantity") int quantity
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
    UPDATE item i
    JOIN contract_with_item ci ON i.id = ci.item_id
    SET i.status = 'P'
    WHERE ci.contract_id = :contractId
      AND i.status = 'S'
""", nativeQuery = true)
    int rollbackItemsToPending(@Param("contractId") Long contractId);

    @Modifying
    @Query(value = """
    UPDATE item i
    JOIN contract_with_item cwi ON cwi.item_id = i.id
    SET i.sales = COALESCE(i.sales, 0) + i.monthly_price
    WHERE cwi.contract_id = :contractId
      AND i.status = 'S'
""", nativeQuery = true)
    int addMonthlySalesByContract(@Param("contractId") Long contractId);

    @Modifying
    @Query(value = """
    UPDATE item i
    JOIN contract_with_item cwi ON cwi.item_id = i.id
    SET i.status = 'O'
    WHERE cwi.contract_id = :contractId
      AND i.status <> 'R'
""", nativeQuery = true)
    int updateItemsToOverdueExceptRepair(
            @Param("contractId") Long contractId
    );

    @Modifying
    @Query(value = """
    UPDATE item i
    JOIN contract_with_item cwi ON cwi.item_id = i.id
    SET i.status = 'O'
    WHERE cwi.contract_id = :contractId
      AND i.status = 'S'
""", nativeQuery = true)
    int updateItemsToOverdueExceptRepairAndStatus(
            @Param("contractId") Long contractId
    );
}
