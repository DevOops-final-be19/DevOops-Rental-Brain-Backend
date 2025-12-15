package com.devoops.rentalbrain.customer.channel.command.controller;

import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandCreateDTO;
import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandResponseDTO;
import com.devoops.rentalbrain.customer.channel.command.service.ChannelCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channel")
@Slf4j
public class ChannelCommandController {
    private final ChannelCommandService channelCommandService;

    @Autowired
    public ChannelCommandController(ChannelCommandService channelCommandService) {
        this.channelCommandService = channelCommandService;
    }

    // 채널 생성
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
    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable Long channelId) {

        log.info("채널 삭제 요청 channelId={}", channelId);

        channelCommandService.deleteChannel(channelId);

        log.info("채널 삭제 완료 channelId={}", channelId);

        return ResponseEntity.noContent().build();
    }


}
