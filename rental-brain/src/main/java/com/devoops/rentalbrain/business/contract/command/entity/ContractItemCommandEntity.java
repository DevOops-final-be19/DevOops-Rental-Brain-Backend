package com.devoops.rentalbrain.business.contract.command.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contract_with_item")
@Data
public class ContractItemCommandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;


}
