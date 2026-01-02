package com.devoops.rentalbrain.business.contract.query.dto;

import com.devoops.rentalbrain.common.pagination.Criteria;
import lombok.Data;

@Data
public class ContractSearchDTO extends Criteria {
    private String contractCode;
    private String cusName;
    private String inCharge;
    private String conName;
    private String status;

    public ContractSearchDTO(int page, int size) {
        super(page, size);
    }
}
