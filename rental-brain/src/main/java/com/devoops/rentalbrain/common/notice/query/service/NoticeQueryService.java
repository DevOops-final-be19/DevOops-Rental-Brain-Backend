package com.devoops.rentalbrain.common.notice.query.service;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;

import java.util.List;

public interface NoticeQueryService {
    List<NoticeReceiveDTO> getNewNoticeList(Long empId);

    PageResponseDTO<NoticeReceiveDTO> getAllNoticeList(Long empId, int size, int page, String type);
}
