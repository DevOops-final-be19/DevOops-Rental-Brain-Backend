package com.devoops.rentalbrain.customer.channel.query.controller;

import com.devoops.rentalbrain.customer.channel.query.dto.ChannelKpiQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelTotalSumDTO;
import com.devoops.rentalbrain.customer.channel.query.service.ChannelQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/channel")
@Tag(
        name = "채널 조회(Query)",
        description = "채널 목록 조회, 채널별 KPI(건수), 전체 합계 조회 등 조회(Query) API"
)
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
    @Operation(
            summary = "채널 목록 조회",
            description = "채널 목록을 조회합니다. channelName을 주면 해당 키워드로 필터링합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/list")
    public ResponseEntity<List<ChannelQueryDTO>> selectChannelList(
                                                                @RequestParam(required = false) String channelName
    ) {
        List<ChannelQueryDTO> list = channelQueryService.selectChannelList(channelName);
        return ResponseEntity.ok(list);
    }

    // kpi에 집어 넣을 만한, 각 채널에 대한 count
    @Operation(
            summary = "채널 KPI 조회",
            description = "채널별 건수(KPI)를 조회합니다. channelName을 주면 해당 키워드로 필터링합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/kpi")
    public ResponseEntity<List<ChannelKpiQueryDTO>> selectChannelKpi(
                                                                  @RequestParam(required = false) String channelName
    ) {
        List<ChannelKpiQueryDTO> list = channelQueryService.selectChannelKpi(channelName);
        return ResponseEntity.ok(list);
    }

    // 혹시 몰라서 전체 totalsum -> 각 테이블에 대한 전체 채널 합계
    @Operation(
            summary = "채널 전체 합계 조회",
            description = "전체 채널 기준 합계/요약 정보를 조회합니다. (대시보드 카드/요약 지표용)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/totalSum")
    public ResponseEntity<ChannelTotalSumDTO> selectChannelTotalSum() {

        ChannelTotalSumDTO summary = channelQueryService.selectChannelTotalSum();
        return ResponseEntity.ok(summary);
    }
}
