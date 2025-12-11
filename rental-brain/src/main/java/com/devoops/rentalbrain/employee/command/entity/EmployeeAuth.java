package com.devoops.rentalbrain.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="employee_auth")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long authId;
    @Column
    private Long empId;
}
