package com.devoops.rentalbrain.common.notice.query.controller;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;
import com.devoops.rentalbrain.common.notice.query.service.NoticeQueryService;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@Slf4j
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
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<NoticeReceiveDTO>> getAllNoticeList(
            @RequestParam Long empId,
            @RequestParam int size,
            @RequestParam int page,
            @RequestParam String type
    ) {
        log.info("{} {} {} {}",empId,size,page,type);
        PageResponseDTO<NoticeReceiveDTO> result = noticeQueryService.getAllNoticeList(empId,size,page,type);
        return ResponseEntity.ok().body(result);
    }
}
