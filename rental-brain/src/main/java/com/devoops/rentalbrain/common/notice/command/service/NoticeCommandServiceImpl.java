package com.devoops.rentalbrain.common.notice.command.service;

import com.devoops.rentalbrain.common.notice.application.domain.PositionType;
import com.devoops.rentalbrain.common.notice.application.strategy.event.NotificationEvent;
import com.devoops.rentalbrain.common.notice.command.dto.NoticeDeleteDTO;
import com.devoops.rentalbrain.common.notice.command.dto.NoticeReadDTO;
import com.devoops.rentalbrain.common.notice.command.entity.Notification;
import com.devoops.rentalbrain.common.notice.command.entity.NotificationReceiver;
import com.devoops.rentalbrain.common.notice.command.repository.NotificationReceiverRepository;
import com.devoops.rentalbrain.common.notice.command.repository.NotificationRepository;
import com.devoops.rentalbrain.common.notice.query.mapper.NoticeQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class NoticeCommandServiceImpl implements NoticeCommandService {
    private final NotificationRepository notificationRepository;
    private final NotificationReceiverRepository notificationReceiverRepository;
    private final NoticeQueryMapper noticeQueryMapper;

    public NoticeCommandServiceImpl(NotificationRepository notificationRepository,
                                    NotificationReceiverRepository notificationReceiverRepository,
                                    NoticeQueryMapper noticeQueryMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationReceiverRepository = notificationReceiverRepository;
        this.noticeQueryMapper = noticeQueryMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void noticeCreate(Notification notification, Long empId) {
        Notification getNotice = notificationRepository.save(notification);
        log.info("알림 생성 - {}", getNotice);

        log.info(notification.toString());
        notificationReceiverRepository.save(
                NotificationReceiver.create(
                        notification.getId(),
                        empId
                )
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void noticeAllCreate(Notification notification, List<PositionType> positionId) {
        Notification getNotice = notificationRepository.save(notification);
        log.info("알림 생성 - {}", getNotice);

        log.info(getNotice.toString());

        List<Long> positions = new ArrayList<>();

        for(PositionType positionType : positionId){
            positions.addAll(noticeQueryMapper.getEmployeeIds(positionType.positionNum()));
        }
        positions.forEach(empId->{
            notificationReceiverRepository.save(
                    NotificationReceiver.create(
                            notification.getId(),
                            empId
                    )
            );
        });
    }

    @Override
    @Transactional
    public void readNotice(NoticeReadDTO noticeReadDTO) {
        try {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            notificationReceiverRepository.findAllById(noticeReadDTO.getNoticeId()).forEach(noticeId->{
                if (noticeId.getIsRead() == 'N') {
                    noticeId.setIsRead('Y');
                    noticeId.setReadAt(now);
                    notificationReceiverRepository.save(noticeId);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException("오류 발생 " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteNotice(NoticeDeleteDTO noticeDeleteDTO) {
        try {
            if(noticeDeleteDTO.getNoticeId().size()==1){
                notificationReceiverRepository.deleteById(noticeDeleteDTO.getNoticeId().get(0));
            }
            else{

                notificationReceiverRepository.deleteAllByIdInBatch(noticeDeleteDTO.getNoticeId());
            }
        } catch (Exception e) {
            throw new RuntimeException("오류 발생 " + e.getMessage());
        }
    }
}
