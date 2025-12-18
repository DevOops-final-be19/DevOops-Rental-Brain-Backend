package com.devoops.rentalbrain.common.notice.command.controller;

import com.devoops.rentalbrain.common.notice.command.dto.NoticeDeleteDTO;
import com.devoops.rentalbrain.common.notice.command.dto.NoticeReadDTO;
import com.devoops.rentalbrain.common.notice.command.service.NoticeCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
public class NoticeCommandController {
    private final NoticeCommandService noticeCommandService;

    public NoticeCommandController(NoticeCommandService noticeCommandService) {
        this.noticeCommandService = noticeCommandService;
    }

    @PutMapping("/read")
    public ResponseEntity<?> readNotice(@RequestBody NoticeReadDTO noticeReadDTO){
        noticeCommandService.readNotice(noticeReadDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteNotice(@RequestBody NoticeDeleteDTO noticeDeleteDTO){
        noticeCommandService.deleteNotice(noticeDeleteDTO);
        return ResponseEntity.ok().build();
    }
}
