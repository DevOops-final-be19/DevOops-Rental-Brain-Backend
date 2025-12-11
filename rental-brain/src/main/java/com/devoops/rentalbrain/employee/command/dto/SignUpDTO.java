package com.devoops.rentalbrain.employee.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignUpDTO {
    private String empId;
    private String pwd;
    private String name;
    private String phone;
    private String email;
    private String addr;
    private String birthday;
    private Character gender;
    private String dept;
    private String hireDate;
    private Long positionId;
}
