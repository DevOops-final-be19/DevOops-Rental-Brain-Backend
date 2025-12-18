package com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface CustomerChurnSnapshotQueryMapper {

    int countTotalCustomers();

    int countMonthRiskCustomers(@Param("snapshotMonth") String snapshotMonth);

    List<String> findMonthsBetween(@Param("fromMonth") String fromMonth,
                                   @Param("toMonth") String toMonth);
}
