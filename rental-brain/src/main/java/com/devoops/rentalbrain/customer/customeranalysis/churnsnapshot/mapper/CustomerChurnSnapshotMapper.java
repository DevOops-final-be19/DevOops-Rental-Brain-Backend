package com.devoops.rentalbrain.customer.customeranalysis.churnsnapshot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerChurnSnapshotMapper {

    int upsertMonthlySnapshot(@Param("month") String month);

    int countDistinctCustomersByMonth(@Param("month") String month);

    int countRiskCustomersByMonth(@Param("month") String month);

    int deleteByMonth(@Param("month") String month);
}