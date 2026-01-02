package com.devoops.rentalbrain.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="emp_position")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmpPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String positionName;
    @Column
    private String description;
}
