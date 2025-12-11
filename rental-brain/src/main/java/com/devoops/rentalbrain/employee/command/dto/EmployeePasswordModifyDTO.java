package com.devoops.rentalbrain.employee.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeePasswordModifyDTO {
    private String empId;
    private String pwd;
    private String modifiedPwd;
}
