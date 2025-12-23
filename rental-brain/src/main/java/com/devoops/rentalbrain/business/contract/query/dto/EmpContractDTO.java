package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

@Data
public class EmpContractDTO {
    private long id;
    private String employeeCode;
    private String name;
    private String dept;
    private long positionId;
    private String positionName;
    private String description;
}
