package com.devoops.rentalbrain.product.maintenance.query.mapper;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDetailDTO;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDTO;
import com.devoops.rentalbrain.product.maintenance.query.dto.NextWeekScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AfterServiceMapper {
    // 목록
    List<AfterServiceDTO> findAll();

    List<AfterServiceDTO> findAllWithPaging( int offset, int size, String type, String status, String keyword, String summaryType, String sortField, String sortOrder );

    long countAll( String type, String status, String keyword, String summaryType );

    // 상세
    AfterServiceDetailDTO findById(Long id);

    // summary
    int countThisMonthSchedule();
    int countImminent72h();
    int countThisMonthCompleted();
    int countTodayInProgress();

    // 다음 주
    int countNextWeekSchedule();
    List<NextWeekScheduleDTO> findNextWeekScheduleList();
}
