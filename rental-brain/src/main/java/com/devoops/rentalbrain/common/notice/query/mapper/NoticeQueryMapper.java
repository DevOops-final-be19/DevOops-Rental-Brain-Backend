package com.devoops.rentalbrain.common.notice.query.mapper;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeQueryMapper {
    List<Long> getEmployeeIds(Long id);

    List<NoticeReceiveDTO> getNewNoticeList(Long empId);

    List<NoticeReceiveDTO> getAllNoticeList(Long empId);
}
