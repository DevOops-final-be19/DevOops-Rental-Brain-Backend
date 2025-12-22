package com.devoops.rentalbrain.common.notice.command.controller;

import com.devoops.rentalbrain.common.notice.command.dto.NoticeDeleteDTO;
import com.devoops.rentalbrain.common.notice.command.dto.NoticeReadDTO;
import com.devoops.rentalbrain.common.notice.command.service.NoticeCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@Tag(name = "알림(Command)",
        description = "알림 읽음,삭제 명령(Command) API")
public class NoticeCommandController {
    private final NoticeCommandService noticeCommandService;

    public NoticeCommandController(NoticeCommandService noticeCommandService) {
        this.noticeCommandService = noticeCommandService;
    }

    @Operation(
            summary = "알림 읽음 처리",
            description = "알림을 읽음 처리 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "읽음")
            }
    )
    @PutMapping("/read")
    public ResponseEntity<?> readNotice(@RequestBody NoticeReadDTO noticeReadDTO){
        noticeCommandService.readNotice(noticeReadDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "알림 삭제 처리",
            description = "알림을 삭제 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 완료")
            }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteNotice(@RequestBody NoticeDeleteDTO noticeDeleteDTO){
        noticeCommandService.deleteNotice(noticeDeleteDTO);
        return ResponseEntity.ok().build();
    }
}
