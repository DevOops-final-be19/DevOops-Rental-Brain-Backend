package com.devoops.rentalbrain.employee.command.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeAuthDTO {
    private Long emp_id;
    private List<Long> auth_id;
}
