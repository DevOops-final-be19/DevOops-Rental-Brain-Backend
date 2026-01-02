package com.devoops.rentalbrain.common.notice.query.service;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;
import com.devoops.rentalbrain.common.notice.query.mapper.NoticeQueryMapper;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
    public PageResponseDTO<NoticeReceiveDTO> getAllNoticeList(Long empId, int size, int page, String type) {
        int offset = (page - 1) * size;
        List<NoticeReceiveDTO> noticeReceiveDTOs = noticeQueryMapper.getAllNoticeList(empId, size, offset, type);

        Long totalCount = noticeQueryMapper.noticeCount(empId, type);

        PagingButtonInfo pagingButtonInfo = Pagination.getPagingButtonInfo(
                new Criteria(page, size), totalCount
        );
        return new PageResponseDTO<>(noticeReceiveDTOs, totalCount, pagingButtonInfo);
    }

}
