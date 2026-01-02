package com.devoops.rentalbrain.employee.query.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeListDTO {
    private Long id;
    private String employeeCode;
    private String emp_id;
    private String name;
    private String phone;
    private String email;
    private String addr;
    private String birthday;
    private Character gender;
    private Character status;
    private String dept;
    private String hire_date;
    private String resign_date;
    private PositionDTO position;
    private List<EmpAuthDTO> empAuth;
}
