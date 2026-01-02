package com.devoops.rentalbrain.product.maintenance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AfterServiceSearchDTO {

    private List<AfterServiceDTO> content;
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
}
