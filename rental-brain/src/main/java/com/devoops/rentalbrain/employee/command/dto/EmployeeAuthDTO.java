package com.devoops.rentalbrain.employee.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeAuthDTO {
    private Long auth_id;
    private Long emp_id;
}
