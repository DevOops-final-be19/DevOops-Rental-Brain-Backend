package com.devoops.rentalbrain.employee.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeInfoDTO {
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
    private PositionDTO position;
}
