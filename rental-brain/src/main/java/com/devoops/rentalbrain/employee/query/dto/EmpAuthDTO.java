package com.devoops.rentalbrain.employee.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmpAuthDTO {
    private Long employee_auth_id;
    private Long auth_id;
    private Long emp_id;
}
