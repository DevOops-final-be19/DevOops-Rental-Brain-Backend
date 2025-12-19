package com.devoops.rentalbrain.common.notice.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NoticeReceiveDTO {
    private Long id;
    private Character isRead;
    private String createdAt;
    private String readAt;
    private Long empId;
    private NoticeDTO notice;
}
