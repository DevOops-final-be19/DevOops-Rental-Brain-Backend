package com.devoops.rentalbrain.employee.query.dto;

import lombok.Data;

@Data
public class ContractEmpDTO {
    private long id;
    private String employeeCode;
    private String name;
    private String dept;
    private long positionId;
    private String positionName;
    private String description;
}
