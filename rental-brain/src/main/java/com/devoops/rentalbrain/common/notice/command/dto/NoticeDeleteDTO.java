package com.devoops.rentalbrain.common.notice.command.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NoticeDeleteDTO {
    private List<Long> noticeId;
}
