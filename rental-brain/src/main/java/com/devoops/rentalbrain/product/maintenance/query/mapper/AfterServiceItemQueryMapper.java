package com.devoops.rentalbrain.product.maintenance.query.mapper;

import com.devoops.rentalbrain.product.maintenance.query.dto.RentalItemForAsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@Mapper
@MapperScan({
        "com.devoops.rentalbrain.business",
        "com.devoops.rentalbrain.product"
})
public interface AfterServiceItemQueryMapper {
    List<RentalItemForAsDTO> selectRentalItemsByCustomer(
            @Param("customerId") Long customerId
    );
}
