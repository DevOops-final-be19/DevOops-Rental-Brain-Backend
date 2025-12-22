package com.devoops.rentalbrain.common.notice.query.mapper;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeQueryMapper {
    List<Long> getEmployeeIds(Long id);

    List<NoticeReceiveDTO> getNewNoticeList(Long empId);

    List<NoticeReceiveDTO> getAllNoticeList(
            @Param("empId") Long empId,
            @Param("size") int size,
            @Param("offset") int offset,
            @Param("type") String type
    );

    Long noticeCount(
            @Param("empId") Long empId,
            @Param("type") String type
    );
}
