package com.devoops.rentalbrain.product.maintenance.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "after_service")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AfterService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String engineer;

    @Column(length = 1, nullable = false)
    private String type;   // R:정기점검, A:AS

    private LocalDateTime dueDate;

    @Column(length = 1, nullable = false)
    private String status; // P:예정, C:완료

    private String contents;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "cum_id")
    private Long customerId;
}
