package com.devoops.rentalbrain.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="employee")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employee_code", nullable = false, unique = true)
    private String employeeCode;
    @Column
    private String empId;
    @Column
    private String pwd;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String addr;
    @Column
    private String birthday;
    @Column
    private Character gender;
    @Column
    private Character status;
    @Column
    private String dept;
    @Column
    private String hireDate;
    @Column
    private String resignDate;
    @Column
    private Long positionId;
}
