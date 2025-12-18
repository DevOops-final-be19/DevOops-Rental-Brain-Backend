package com.devoops.rentalbrain.common.notice.query.controller;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;
import com.devoops.rentalbrain.common.notice.query.service.NoticeQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
@Tag(name = "알림(Query)",
        description = "알림 조회(Query) API")
public class NoticeQueryController {
    private final NoticeQueryService noticeQueryService;

    public NoticeQueryController(NoticeQueryService noticeQueryService) {
        this.noticeQueryService = noticeQueryService;
    }

    @Operation(
            summary = "새로운 알림 조회",
            description = "읽지 않은 알림을 조회 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회")
            }
    )
    @GetMapping("/list/new/{empId}")
    public ResponseEntity<List<NoticeReceiveDTO>> getNewNoticeList(@PathVariable Long empId) {
        List<NoticeReceiveDTO> noticeReceiveDTO = noticeQueryService.getNewNoticeList(empId);
        return ResponseEntity.ok().body(noticeReceiveDTO);
    }

    @Operation(
            summary = "전체 알림 조회",
            description = "모든 알림을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회")
            }
    )
    @GetMapping("/list/{empId}")
    public ResponseEntity<List<NoticeReceiveDTO>> getAllNoticeList(@PathVariable Long empId) {
        List<NoticeReceiveDTO> noticeReceiveDTO = noticeQueryService.getAllNoticeList(empId);
        return ResponseEntity.ok().body(noticeReceiveDTO);
    }
}
