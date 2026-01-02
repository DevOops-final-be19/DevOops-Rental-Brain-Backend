package com.devoops.rentalbrain.employee.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDetailInfoDTO {
    private Long id;
    private String employeeCode;
    private String empId;
    private String name;
    private String phone;
    private String email;
    private String addr;
    private String birthday;
    private Character gender;
    private Character status;
    private String dept;
    private String hireDate;
    private String resignDate;
    private Long positionId;
}
