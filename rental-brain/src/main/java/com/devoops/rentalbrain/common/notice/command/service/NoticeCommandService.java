package com.devoops.rentalbrain.common.notice.command.service;

import com.devoops.rentalbrain.common.notice.application.domain.PositionType;
import com.devoops.rentalbrain.common.notice.application.strategy.event.NotificationEvent;
import com.devoops.rentalbrain.common.notice.command.dto.NoticeDeleteDTO;
import com.devoops.rentalbrain.common.notice.command.dto.NoticeReadDTO;
import com.devoops.rentalbrain.common.notice.command.entity.Notification;

public interface NoticeCommandService {
    void noticeCreate(Notification notification,Long empId);
    void noticeAllCreate(Notification notification, PositionType positionId);

    void readNotice(NoticeReadDTO noticeReadDTO);

    void deleteNotice(NoticeDeleteDTO noticeDeleteDTO);
}
