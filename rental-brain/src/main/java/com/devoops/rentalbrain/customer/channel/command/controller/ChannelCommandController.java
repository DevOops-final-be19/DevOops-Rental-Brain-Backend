package com.devoops.rentalbrain.customer.channel.command.controller;

import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandCreateDTO;
import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandResponseDTO;
import com.devoops.rentalbrain.customer.channel.command.service.ChannelCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channel")
@Slf4j
@Tag(
        name = "채널 관리(Command)",
        description = "상담 채널 등록, 수정, 삭제 등 채널 관련 명령(Command) API"
)
public class ChannelCommandController {
    private final ChannelCommandService channelCommandService;

    @Autowired
    public ChannelCommandController(ChannelCommandService channelCommandService) {
        this.channelCommandService = channelCommandService;
    }

    // 채널 생성
    @Operation(
            summary = "채널 등록",
            description = "새로운 상담 채널을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채널 등록 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/insert")
    public ResponseEntity<ChannelCommandCreateDTO> insertChannel(
            @RequestBody ChannelCommandCreateDTO  channelCommandCreateDTO) {

        // insert 할 DTO들 저장하는 것
        ChannelCommandCreateDTO saved
                = channelCommandService.insertChannel(channelCommandCreateDTO);

        if (saved == null) {
            log.info("채널 저장 실패: {}", channelCommandCreateDTO);
            throw new IllegalArgumentException("채널 저장 실패했습니다.");
        } else {
            log.info("채널 저장 완료!: {}", saved);
            return ResponseEntity.ok().body(saved);
        }
    }

    // 채널 수정
    @Operation(
            summary = "채널 수정",
            description = "channelId에 해당하는 채널 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채널 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
            @ApiResponse(responseCode = "404", description = "채널 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/update/{channelId}")
    public ResponseEntity<Void> updateChannel(
            @PathVariable Long channelId,
            @RequestBody ChannelCommandResponseDTO channelCommandResponseDTO) {

        log.info("채널 수정 요청 channelId={}, newName={}",
                channelId, channelCommandResponseDTO.getChannelName());

        channelCommandService.updateChannel(channelId, channelCommandResponseDTO);

        log.info("[채널 수정 완료] channelId={}", channelId);

        return ResponseEntity.ok().build();
    }

    // 채널 삭제
    @Operation(
            summary = "채널 삭제",
            description = "channelId에 해당하는 채널 정보를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "채널 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "채널 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable Long channelId) {

        log.info("채널 삭제 요청 channelId={}", channelId);

        channelCommandService.deleteChannel(channelId);

        log.info("채널 삭제 완료 channelId={}", channelId);

        return ResponseEntity.noContent().build();
    }


}
