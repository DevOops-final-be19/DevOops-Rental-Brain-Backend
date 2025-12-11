package com.devoops.rentalbrain.product.maintenance.query.mapper;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AfterServiceMapper {

    List<AfterServiceResponse> findAll();
}
