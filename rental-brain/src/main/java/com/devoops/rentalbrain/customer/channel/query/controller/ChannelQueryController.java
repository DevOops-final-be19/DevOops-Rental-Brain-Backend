package com.devoops.rentalbrain.customer.channel.query.controller;

import com.devoops.rentalbrain.customer.channel.query.dto.ChannelKpiQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelTotalSumDTO;
import com.devoops.rentalbrain.customer.channel.query.service.ChannelQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/channel")
public class ChannelQueryController {

    private final ChannelQueryService channelQueryService;

    @Autowired
    public ChannelQueryController(ChannelQueryService channelQueryService) {
        this.channelQueryService = channelQueryService;
    }

    @GetMapping("/health")
    public String health() {
        return "Channel OK";
    }

    // 채널 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<ChannelQueryDTO>> selectChannelList(
                                                                @RequestParam(required = false) String channelName
    ) {
        List<ChannelQueryDTO> list = channelQueryService.selectChannelList(channelName);
        return ResponseEntity.ok(list);
    }

    // kpi에 집어 넣을 만한, 각 채널에 대한 count
    @GetMapping("/kpi")
    public ResponseEntity<List<ChannelKpiQueryDTO>> selectChannelKpi(
                                                                  @RequestParam(required = false) String channelName
    ) {
        List<ChannelKpiQueryDTO> list = channelQueryService.selectChannelKpi(channelName);
        return ResponseEntity.ok(list);
    }

    // 혹시 몰라서 전체 totalsum -> 각 테이블에 대한 전체 채널 합계
    @GetMapping("/totalSum")
    public ResponseEntity<ChannelTotalSumDTO> selectChannelTotalSum() {

        ChannelTotalSumDTO summary = channelQueryService.selectChannelTotalSum();
        return ResponseEntity.ok(summary);
    }
}
