package com.devoops.rentalbrain.common.notice.query.service;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;
import com.devoops.rentalbrain.common.notice.query.mapper.NoticeQueryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeQueryServiceImpl implements NoticeQueryService {
    private final NoticeQueryMapper noticeQueryMapper;

    public NoticeQueryServiceImpl(NoticeQueryMapper noticeQueryMapper) {
        this.noticeQueryMapper = noticeQueryMapper;
    }

    @Override
    public List<NoticeReceiveDTO> getNewNoticeList(Long empId) {
        return noticeQueryMapper.getNewNoticeList(empId);
    }

    @Override
    public List<NoticeReceiveDTO> getAllNoticeList(Long empId) {
        return noticeQueryMapper.getAllNoticeList(empId);
    }
}
