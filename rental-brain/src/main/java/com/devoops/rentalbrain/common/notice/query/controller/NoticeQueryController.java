package com.devoops.rentalbrain.common.notice.query.controller;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;
import com.devoops.rentalbrain.common.notice.query.service.NoticeQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeQueryController {
    private final NoticeQueryService noticeQueryService;

    public NoticeQueryController(NoticeQueryService noticeQueryService) {
        this.noticeQueryService = noticeQueryService;
    }

    @GetMapping("/list/new/{empId}")
    public ResponseEntity<List<NoticeReceiveDTO>> getNewNoticeList(@PathVariable Long empId) {
        List<NoticeReceiveDTO> noticeReceiveDTO = noticeQueryService.getNewNoticeList(empId);
        return ResponseEntity.ok().body(noticeReceiveDTO);
    }

    @GetMapping("/list/{empId}")
    public ResponseEntity<List<NoticeReceiveDTO>> getAllNoticeList(@PathVariable Long empId) {
        List<NoticeReceiveDTO> noticeReceiveDTO = noticeQueryService.getAllNoticeList(empId);
        return ResponseEntity.ok().body(noticeReceiveDTO);
    }
}
