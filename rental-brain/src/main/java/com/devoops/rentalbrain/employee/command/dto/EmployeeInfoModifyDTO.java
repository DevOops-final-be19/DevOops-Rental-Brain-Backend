package com.devoops.rentalbrain.employee.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeInfoModifyDTO {
    private String empId;
    private String name;
    private String phone;
    private String email;
    private String addr;
}
