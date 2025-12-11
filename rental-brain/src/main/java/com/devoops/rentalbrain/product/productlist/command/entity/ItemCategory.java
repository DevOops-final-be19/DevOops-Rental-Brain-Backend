package com.devoops.rentalbrain.product.productlist.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemCategory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
