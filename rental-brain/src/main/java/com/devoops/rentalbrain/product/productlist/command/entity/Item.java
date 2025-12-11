package com.devoops.rentalbrain.product.productlist.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "serial_num")
    private String serialNum;

    @Column(name = "monthly_price")
    private Integer monthlyPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "last_inspect_date")
    private LocalDateTime lastInspectDate;

    @Column(name = "sales")
    private Integer sales;

    @Column(name = "repair_cost")
    private Integer repairCost;

    @Column(name = "category_id")
    private Long categoryId;
}

