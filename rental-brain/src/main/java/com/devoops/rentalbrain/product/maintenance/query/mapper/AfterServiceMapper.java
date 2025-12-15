package com.devoops.rentalbrain.product.maintenance.query.mapper;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDetailResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.NextWeekScheduleResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AfterServiceMapper {
    // 목록
    List<AfterServiceResponse> findAll();

    // 상세
    AfterServiceDetailResponse findById(Long id);

    // summary
    int countThisMonthSchedule();
    int countImminent72h();
    int countThisMonthCompleted();
    int countTodayInProgress();

    // 다음 주
    int countNextWeekSchedule();
    List<NextWeekScheduleResponse> findNextWeekScheduleList();
}
