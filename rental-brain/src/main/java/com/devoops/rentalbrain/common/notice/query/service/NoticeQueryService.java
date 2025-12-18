package com.devoops.rentalbrain.common.notice.query.service;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;

import java.util.List;

public interface NoticeQueryService {
    List<NoticeReceiveDTO> getNewNoticeList(Long empId);

    List<NoticeReceiveDTO> getAllNoticeList(Long empId);
}
