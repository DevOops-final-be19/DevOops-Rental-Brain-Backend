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

    @Column(name = "after_service_code", nullable = false, unique = true)
    private String after_service_code;

    @Column
    private String engineer;

    @Column(length = 1, nullable = false)
    private String type;   // R:정기점검, A:AS

    @Column
    private LocalDateTime dueDate;

    @Column(length = 1, nullable = false)
    private String status; // P:예정, C:완료

    @Column
    private String contents;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "cum_id")
    private Long customerId;
}
